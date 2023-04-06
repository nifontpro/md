import {FC, PropsWithChildren} from 'react';

const AuthProvider: FC<PropsWithChildren> = ({children}) => {

    // const {isAuth} = useAuthState()
    // const {push} = useRouter();

    // useEffect(() => {
    //     if (!isAuth) {
    //         push("/login").then()
    //     }
    //     // eslint-disable-next-line react-hooks/exhaustive-deps
    // }, [isAuth])

    return <>{children}</>;
};

export default AuthProvider;
