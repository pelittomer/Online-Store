import { FormInputText } from '../../../../../common/components/form/FormInputText'
import type { BrandFormValues } from '../../schema/brand.create.schema'

function FormBody({ setImageFile }) {
    const handleImageChange = (event) => {
        if (event.target.files && event.target.files[0]) {
            setImageFile(event.target.files[0])
        } else {
            setImageFile(null)
        }
    }
    return (
        <div>
            <FormInputText<BrandFormValues>
                name='name'
                label='Brand Name'
                type='text'
                placeholder='Adidas'
            />
            <div>
                <label htmlFor="image" className="block text-sm font-medium text-gray-700">Brand Image</label>
                <input
                    id="image"
                    name="image"
                    type="file"
                    accept="image/*"
                    onChange={handleImageChange}
                    className="mt-1 block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 focus:outline-none p-5"
                />
            </div>
        </div>
    )
}

export default FormBody