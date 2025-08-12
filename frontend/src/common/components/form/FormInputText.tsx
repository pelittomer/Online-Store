
import TextField, { type TextFieldProps } from "@mui/material/TextField";
import { Controller, useFormContext, type FieldValues, type Path } from "react-hook-form";

type Props<T extends FieldValues> = {
    name: Path<T>;
} & Pick<TextFieldProps, 'label' | 'type' | 'placeholder' | 'variant' | 'fullWidth' | 'margin' | 'autoComplete'>


export function FormInputText<T extends FieldValues>({
    name,
    ...props
}: Props<T>) {
    const { control } = useFormContext();

    return (
        <Controller
            name={name}
            control={control}
            render={({ field, fieldState: { error } }) => (
                <TextField
                    {...field}
                    {...props}
                    error={!!error}
                    helperText={error?.message || ''}
                    variant="outlined"
                    fullWidth
                    margin="normal"
                />
            )}
        />
    );
}