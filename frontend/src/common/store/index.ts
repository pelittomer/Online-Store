import { configureStore } from '@reduxjs/toolkit'
import { setupListeners } from '@reduxjs/toolkit/query'
import { apiSlice } from './apiSlice'
import AuthReducer from './authSlice'

export const store = configureStore({
    reducer: {
        [apiSlice.reducerPath]: apiSlice.reducer,
        auth: AuthReducer,
    },
    middleware: getDefaultMiddleware =>
        getDefaultMiddleware().concat(),
    devTools: false
})

setupListeners(store.dispatch)