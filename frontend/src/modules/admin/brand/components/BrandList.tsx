import React from 'react'
import { useGetbrandsQuery } from '../../../../common/services/BrandApiSlice'
import BrandListItem from './BrandListItem';

function BrandList() {
    const { data: brands, isLoading, isSuccess } = useGetbrandsQuery('brandsList')
    let content;
    if (isLoading) <p>Loading...</p>
    if (isSuccess) {
        const { ids } = brands
        const tableContent = ids?.length && ids.map((brandId, index) => <BrandListItem
            key={brandId}
            brandId={brandId}
            index={index + 1} />)
        content = (
            <div>
                <h3>Brand LİST</h3>
                {tableContent}
            </div>
        )
    }

    return content
}

export default BrandList