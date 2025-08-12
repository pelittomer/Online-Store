import { apiSlice } from "../store/apiSlice";
import { logout, setCredentials } from "../store/authSlice";

export const authApiSlice = apiSlice.injectEndpoints({
    endpoints: builder => ({
        register: builder.mutation({
            query: ({ payload, role }) => ({
                url: `/user/auth/${role}/sign-up`,
                method: 'POST',
                body: { ...payload }
            }),
        }),
        login: builder.mutation({
            query: ({ payload, role }) => ({
                url: `/user/auth/${role}/sign-in`,
                method: 'POST',
                body: { ...payload }
            })
        }),
        sendLogout: builder.mutation({
            query: () => ({
                url: '/user/auth/sign-out',
                method: 'POST'
            }),
            async onQueryStarted(arg, { dispatch, queryFulfilled }) {
                try {
                    const { data } = await queryFulfilled
                    
                    dispatch(logout())
                    setTimeout(() => {
                        dispatch(apiSlice.util.resetApiState())
                    }, 1000);
                } catch (error) {
                    console.log("sign out error :", error)
                }
            }
        }),
        refresh: builder.mutation({
            query: () => ({
                url: '/user/auth/refresh',
                method: 'GET'
            }),
            async onQueryStarted(arg, { dispatch, queryFulfilled }) {
                try {
                    const { data } = await queryFulfilled
                    dispatch(setCredentials({ accessToken: data.data }))
                } catch (error) {
                    console.log("refresh error : ", error)
                }
            }
        })
    })
})

export const {
    useLoginMutation,
    useSendLogoutMutation,
    useRefreshMutation,
    useRegisterMutation
} = authApiSlice