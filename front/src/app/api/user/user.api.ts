import {createApi} from '@reduxjs/toolkit/dist/query/react';
import {baseQueryWithReauth} from "@/app/api/base/base.api";
import {User} from "@/app/domain/model/user/user";
import {UserDetails} from "@/app/domain/model/user/userDetails";
import {CreateOwnerRequest} from "@/app/api/user/request/CreateOwnerRequest";

export const userApi = createApi({
    reducerPath: 'UserApi',
    baseQuery: baseQueryWithReauth,
    tagTypes: ['User'],
    endpoints: (build) => ({

        getTestData: build.query<{ res: string }, void>({
            query: () => {
                return {
                    method: 'POST',
                    url: '/user/data',
                    body: {res: "Test Body from Front"}
                }
            },
            providesTags: ['User']
        }),

        /**
         * Возвращает профили вошедшего в систему пользователя
         */
        getProfiles: build.query<User[], void>({
            query: () => {
                return {
                    method: 'POST',
                    url: '/user/profiles',
                }
            },
            providesTags: ['User']
        }),

        createOwner: build.mutation<UserDetails, CreateOwnerRequest>({
            query: (request) => {
                return {
                    method: 'POST',
                    url: '/user/create_owner',
                    body: request
                }
            },
            invalidatesTags: ['User']
        }),

    })
})