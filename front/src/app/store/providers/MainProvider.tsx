import {FC, PropsWithChildren} from 'react'
import {Provider} from "react-redux";
import {persistor, store} from "@/app/store/storage/store";
import {PersistGate} from 'redux-persist/integration/react'
import AuthProvider from "@/app/store/providers/AuthProvider";

// https://www.npmjs.com/package/react-keycloak-id
// https://mobihack.me/blog/2021-12-31-keycloak-nextjs/

const MainProvider: FC<PropsWithChildren> = ({children}) => {
    return (

        <Provider store={store}>
            <PersistGate persistor={persistor} loading={null}>
                <AuthProvider>
                    {children}
                </AuthProvider>
            </PersistGate>
        </Provider>
    )
}

export default MainProvider
