import { Outlet, Link } from "react-router"
import { useEffect } from 'react'
import { useSelector } from 'react-redux'
import { selectCurrentToken } from "../store/authSlice"
import { useRefreshMutation } from "../services/AuthApiSlice"

const PersistLogin = () => {
    const token = useSelector(selectCurrentToken)
    const [refresh, {
        isUninitialized,
        isLoading,
        isSuccess,
        isError,
        error
    }] = useRefreshMutation()

    useEffect(() => {
        const verifyRefreshToken = async () => {
            try {
                await refresh()
            } catch (err) {
                console.error("Refresh token failed:", err)
            }
        };

        if (!token) verifyRefreshToken()
    }, [token])

    let content
    if (isLoading) {
        content = <p>Loading...</p>
    } else if (isError) {
        content = (
            <p className='errmsg'>
                {error.data?.message}
                <Link to="/auth/customer/sign-in">Please login again</Link>.
            </p>
        )
    } else if (isSuccess) {
        content = <Outlet />
    } else if (token && isUninitialized) {
        content = <Outlet />
    }

    return content
}
export default PersistLogin