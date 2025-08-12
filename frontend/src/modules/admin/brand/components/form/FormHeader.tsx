import React from 'react'

function FormHeader({ errorMessage }) {
    return (
        <div>
            <h3>Brand form</h3>
            <p>{errorMessage}</p>
        </div>
    )
}

export default FormHeader