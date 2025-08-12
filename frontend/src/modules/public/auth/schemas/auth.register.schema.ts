import { z } from "zod"
import { VALIDATION_MESSAGES } from "../constants/auth-validation-message"

export const RegisterValidationSchema = z.object({
    email: z
        .string()
        .min(1, { message: VALIDATION_MESSAGES.EMAIL_REQUIRED })
        .email({ message: VALIDATION_MESSAGES.EMAIL_INVALID })
        .max(255, { message: VALIDATION_MESSAGES.EMAIL_MAX_LENGTH })
        .trim(),
    password: z
        .string()
        .min(8, { message: VALIDATION_MESSAGES.PASSWORD_MIN_LENGTH })
        .max(16, { message: VALIDATION_MESSAGES.PASSWORD_MAX_LENGTH })
        .regex(/[A-Z]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_UPPERCASE })
        .regex(/[a-z]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_LOWERCASE })
        .regex(/[0-9]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_DIGIT })
        .regex(/[^a-zA-Z0-9]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_SPECIAL_CHAR }),
    confirmPassword: z.string()
        .min(8, { message: VALIDATION_MESSAGES.PASSWORD_MIN_LENGTH })
        .max(16, { message: VALIDATION_MESSAGES.PASSWORD_MAX_LENGTH })
        .regex(/[A-Z]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_UPPERCASE })
        .regex(/[a-z]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_LOWERCASE })
        .regex(/[0-9]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_DIGIT })
        .regex(/[^a-zA-Z0-9]/, { message: VALIDATION_MESSAGES.PASSWORD_REQUIRE_SPECIAL_CHAR }),
})
export type RegisterFormValues = z.infer<typeof RegisterValidationSchema>

export const registerDefaultValues: RegisterFormValues = {
    email: '',
    password: '',
    confirmPassword: '',
}