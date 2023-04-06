import type {NextPage} from 'next';
import React from "react";
import {resourceApi} from "@/app/resource/data/resource.api";
import {APP_URI, CLIENT_ID, KEYCLOAK_URI} from "@/auth/data/auth.api";
import {useJwt} from "react-jwt";
import {IPayload} from "@/app/resource/model/idToken";
import {useRouter} from "next/router";
import {useDispatch} from "react-redux";
import {authActions, useAuthState} from "@/auth/data/auth.slice";

const Home: NextPage = () => {

    const {push} = useRouter()
    const {isAuth, idToken} = useAuthState()
    const dispatch = useDispatch()
    const {decodedToken, isExpired} = useJwt(idToken || '');
    const payload = decodedToken as IPayload | undefined

    const {data: getInfo} = resourceApi.useGetTestDataQuery(undefined, {skip: !isAuth})

    const logoutHandler = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault()
        const it = localStorage.getItem("it")
        if (it != undefined && !isExpired) {
            logoutWin(it)
        }
        await dispatch(authActions.setNoAccess())
        // await push("/login")
    }

    const loginHandler = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault()
        await push("/login")
    }

    return (
        <div className="flex flex-col m-2 break-all">

            <div className="text-red-700">KEYCLOAK_URL: {process.env.KEYCLOAK_URL}</div>
            <div className="text-green-700">APP_URL: {process.env.APP_URL}</div>
            <div className="text-blue-700">API_SERVER_URL: {process.env.API_SERVER_URL}</div>

            <div className="text-cyan-700">
                {isAuth ? <>Вход выполнен</>: <>Вход не выполнен</>}
            </div>
            <div className="text-red-700">Info from client: {getInfo?.res}</div>
            <button onClick={loginHandler} className="m-3 border-2 text-green-700">
                Login
            </button>
            <button onClick={logoutHandler} className="m-3 border-2 text-red-700">
                Logout
            </button>
            <div className="text-blue-700">name: {payload?.name}</div>
            <div className="text-green-700">email: {payload?.email}</div>
        </div>
    );

    function logoutWin(it: string) {

        console.log(it)

        const params = [
            'post_logout_redirect_uri=' + APP_URI,
            'id_token=' + it,
            'client_id=' + CLIENT_ID,
        ];

        const url = KEYCLOAK_URI + '/logout' + '?' + params.join('&')
        window.open(url, '_self');
    }

};

export default Home;