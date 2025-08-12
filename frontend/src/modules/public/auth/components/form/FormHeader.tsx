import type { AuthFormMethod } from '../../types'

interface FormHeaderProps {
  method: AuthFormMethod;
  errorMessage: string | null;
}

function FormHeader({ method, errorMessage }: FormHeaderProps) {
  return (
    <div>
      <div>
        <h1 >
          {method === 'sign-in' ? 'Sign In to Your Account' : 'Create a New Account'}
        </h1>
        <p>
          {method === 'sign-in'
            ? 'Enter your email and password to continue.'
            : 'Sign up quickly to access our services.'}
        </p>
      </div>
      <div>
        <h3>{errorMessage}</h3>
      </div>
    </div>
  )
}

export default FormHeader