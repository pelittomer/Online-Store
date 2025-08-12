import { FormProvider, useForm } from "react-hook-form"
import { useParams } from 'react-router'
import { zodResolver } from "@hookform/resolvers/zod"
import { AuthDefaultValues, AuthSchemas, type AuthMethodMap } from "./types"
import { Role, type RoleType } from "../../../common/types"
import AuthForm from "./components/AuthForm"

function page() {
    const { role, method } = useParams<{ role?: RoleType; method?: keyof AuthMethodMap }>()
    const currentRole = role === Role.Seller ? Role.Seller : Role.Customer
    const resolvedMethod = method === "sign-in" ? "sign-in" : "sign-up"
    const methods = useForm<AuthMethodMap[typeof resolvedMethod]>({
        mode: 'all',
        resolver: zodResolver(AuthSchemas[resolvedMethod]),
        defaultValues: AuthDefaultValues[resolvedMethod],
    })
    return (
        <div>
            <h1>Auth page</h1>
            <FormProvider {...methods}>
                <AuthForm role={currentRole} method={resolvedMethod} />
            </FormProvider>
        </div>
    )
}

export default page