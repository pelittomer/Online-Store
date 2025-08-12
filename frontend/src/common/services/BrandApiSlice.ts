import { createEntityAdapter, createSelector } from "@reduxjs/toolkit";
import { apiSlice } from "../store/apiSlice";

const brandAdapter = createEntityAdapter({})
const initialState = brandAdapter.getInitialState()

export const brandApiSlice = apiSlice.injectEndpoints({
    endpoints: builder => ({
        getbrands: builder.query({
            query: () => ({
                url: '/brand',
                method: 'GET'
            }),
            transformResponse: responseData => {
                const { data } = responseData
                const loadedBrands = data.map(brand => {
                    brand.id = brand.id
                    return brand
                })
                return brandAdapter.setAll(initialState, loadedBrands)
            },
            providesTags: (result, error, arg) => {
                if (result?.ids) {
                    return [
                        { type: 'Brand' as const, id: 'LIST' },
                        ...result.ids.map(id => ({ type: 'Brand' as const, id }))
                    ]
                } else {
                    return [{ type: 'Brand' as const, id: 'LIST' }]
                }
            }
        }),
        addNewBrand: builder.mutation<any, any>({
            query: initialData => ({
                url: '/brand',
                method: 'POST',
                body: initialData,
            }),
            invalidatesTags: [
                { type: "Brand", id: "LIST" }
            ]
        })
    })
})

export const {
    useGetbrandsQuery,
    useAddNewBrandMutation
} = brandApiSlice

export const selectBrandResult = brandApiSlice.endpoints.getbrands.select()

const selectBrandData = createSelector(
    selectBrandResult,
    brandResult => brandResult.data
)

export const {
    selectAll: selectAllBrands,
} = brandAdapter.getSelectors(state => selectBrandData(state) ?? initialState)