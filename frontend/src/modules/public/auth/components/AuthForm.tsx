import { useFormContext } from 'react-hook-form';
import type { LoginFormValues } from '../schemas/auth.login.schema';
import type { AuthFormMethod } from '../types';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import type { RegisterFormValues } from '../schemas/auth.register.schema';
import { useLoginMutation, useRegisterMutation } from '../../../../common/services/AuthApiSlice';
import { setCredentials } from '../../../../common/store/authSlice';
import FormHeader from './form/FormHeader';
import FormBody from './form/FormBody';
import FormFooter from './form/FormFooter';
import type { RoleType } from '../../../../common/types';

interface AuthFormProps {
    role: RoleType;
    method: AuthFormMethod;
}

function AuthForm({ role, method }: AuthFormProps) {
    const [errorMessage, setErrorMessage] = useState<string | null>(null)
    const { handleSubmit } = useFormContext<LoginFormValues>()
    const [login, { isLoading: isLoginLoading }] = useLoginMutation()
    const [register, { isLoading: isRegisterLoading }] = useRegisterMutation()

    const dispatch = useDispatch()
    const navigate = useNavigate()

    const currentRole = role === 'SELLER' ? 'SELLER' : 'CUSTOMER'
    const handleSignIn = async (payload: LoginFormValues) => {
        try {
            const { data } = await login({ payload, role: currentRole }).unwrap()
            dispatch(setCredentials({ accessToken: data }))
            if (!isSubmitting) {
                navigate('/')
            }
        } catch (error: any) {
            setErrorMessage(error?.data?.message || 'Failed to sign in. Please check your credentials.')
        }
    }

    const handleSignUp = async (payload: RegisterFormValues) => {
        try {
            await register({ payload, role: currentRole }).unwrap()
            if (!isSubmitting) handleSignIn(payload)
        } catch (error: any) {
            setErrorMessage(error?.data?.message || 'Failed to sign up. Please try again.')
        }
    };

    const onSubmit = async (data: RegisterFormValues | LoginFormValues) => {
        setErrorMessage(null)
        if (method === 'sign-in') {
            await handleSignIn(data)
        } else {
            if ('confirmPassword' in data && data.confirmPassword && data.password !== data.confirmPassword) {
                setErrorMessage('Passwords do not match.')
                return
            }
            await handleSignUp(data as RegisterFormValues)
        }
    }

    const isSubmitting = isLoginLoading || isRegisterLoading
    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <h1>{role} Form</h1>
            <FormHeader method={method} errorMessage={errorMessage} />
            <FormBody method={method} />
            <FormFooter method={method} isSubmitting={isSubmitting} role={role} />
        </form>
    )
}

export default AuthForm