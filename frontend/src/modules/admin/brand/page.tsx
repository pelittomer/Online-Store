import { zodResolver } from '@hookform/resolvers/zod'
import React, { useState } from 'react'
import { FormProvider, useForm } from 'react-hook-form'
import { brandDefaultValues, BrandValidationSchema, type BrandFormValues } from './schema/brand.create.schema'
import Button from '../../../common/components/button/Button'
import BrandForm from './components/BrandForm'
import BrandList from './components/BrandList'

function page() {
    const [openForm, setOpenForm] = useState<boolean>(false)
    const methods = useForm<BrandFormValues>({
        mode: 'all',
        resolver: zodResolver(BrandValidationSchema),
        defaultValues: brandDefaultValues
    })
    return (
        <div>
            <h1>Brand page</h1>
            <Button onClick={() => setOpenForm(true)}>Create</Button>
            {
                openForm && (
                    <FormProvider {...methods}>
                        <BrandForm setOpenForm={setOpenForm} />
                    </FormProvider>
                )
            }
            <BrandList />
        </div>
    )
}

export default page