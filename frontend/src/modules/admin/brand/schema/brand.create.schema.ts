import { z } from "zod"
import { BRAND_VALIDATION_MESSAGES } from "../constants/brand-validation-message";

export const BrandValidationSchema = z.object({
    name: z
        .string()
        .min(1, { message: BRAND_VALIDATION_MESSAGES.NAME_REQUIRED })
        .min(2, { message: BRAND_VALIDATION_MESSAGES.NAME_MIN_LENGTH })
        .max(100, { message: BRAND_VALIDATION_MESSAGES.NAME_MAX_LENGTH })
        .trim(), 
});

export type BrandFormValues = z.infer<typeof BrandValidationSchema>

export const brandDefaultValues: BrandFormValues = {
    name: '',
}