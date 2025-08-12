import React, { useState } from 'react'
import type { BrandFormValues } from '../schema/brand.create.schema'
import { useFormContext } from 'react-hook-form'
import FormHeader from './form/FormHeader'
import FormBody from './form/FormBody'
import FormFooter from './form/FormFooter'
import { useAddNewBrandMutation } from '../../../../common/services/BrandApiSlice'

function BrandForm({ setOpenForm }) {
    const [errorMessage, setErrorMessage] = useState<string>()
    const [imageFile, setImageFile] = useState<File>()

    const { handleSubmit } = useFormContext<BrandFormValues>()
    const [addNewBrand, { isLoading }] = useAddNewBrandMutation()

    const onSubmit = async (data: BrandFormValues) => {
        if (!imageFile) {
            setErrorMessage('Please select an image to upload.')
            return
        }
        const formData = new FormData()
        formData.append('file', imageFile)
        formData.append(
            'brand',
            new Blob([JSON.stringify(data)], {
                type: 'application/json',
            })
        )
        try {
            await addNewBrand(formData).unwrap()
            setOpenForm(false)
        } catch (error) {
            setErrorMessage(error?.data?.message)
        }
    }

    return (
        <div>
            <div>
                <p onClick={() => setOpenForm(false)}>X</p>
            </div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <FormHeader errorMessage={errorMessage} />
                <FormBody setImageFile={setImageFile} />
                <FormFooter isLoading={isLoading} />
            </form>
        </div>
    )
}

export default BrandForm