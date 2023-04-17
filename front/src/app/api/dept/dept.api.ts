import {createApi} from '@reduxjs/toolkit/dist/query/react';
import {baseQueryWithReauth} from "@/app/api/base/base.api";
import {Dept} from "@/app/domain/model/dept/dept";
import {DeptDetails} from "@/app/domain/model/dept/deptDetails";
import {CreateDeptRequest} from "@/app/api/dept/request/createDeptRequest";
import {BaseResponse} from "@/app/domain/model/base/baseResponse";

export const deptApi = createApi({
    reducerPath: 'DeptApi',
    baseQuery: baseQueryWithReauth,
    tagTypes: ['Dept'],
    endpoints: (build) => ({

        /**
         * Получение поддерева отделов текущего пользователя
         */
        getAuthSubtree: build.query<BaseResponse<Dept[]>, void>({
            query: () => {
                return {
                    method: 'POST',
                    url: '/dept/auth_subtree',
                }
            },
            providesTags: ['Dept']
        }),

        /**
         * Создание нового отдела
         */
        getProfiles: build.mutation<BaseResponse<DeptDetails>, CreateDeptRequest>({
            query: () => {
                return {
                    method: 'POST',
                    url: '/dept/create',
                }
            },
            invalidatesTags: ['Dept']
        }),

    })
})