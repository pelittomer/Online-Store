import { loginDefaultValues, LoginValidationSchema, type LoginFormValues } from "../schemas/auth.login.schema";
import { registerDefaultValues, RegisterValidationSchema, type RegisterFormValues } from "../schemas/auth.register.schema";

export type AuthMethodMap = {
    "sign-in": LoginFormValues;
    "sign-up": RegisterFormValues;
}

export const AuthSchemas = {
    "sign-in": LoginValidationSchema,
    "sign-up": RegisterValidationSchema
} as const

export const AuthDefaultValues = {
    "sign-in": loginDefaultValues,
    "sign-up": registerDefaultValues
} as const

export type AuthFormMethod = 'sign-in' | 'sign-up'