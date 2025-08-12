import Button from '../../../../../common/components/button/Button';
import type { RoleType } from '../../../../../common/types';
import type { AuthFormMethod } from '../../types';

interface FormFooterProps {
    method: AuthFormMethod;
    isSubmitting: boolean;
    role: RoleType;
}

function FormFooter({ method, isSubmitting, role }: FormFooterProps) {
    const buttonTxt = method === "sign-in" ? "Login" : "Register"
    return (
        <div>
            <Button className='cursor-pointer'
                type="submit"
                disabled={isSubmitting}>
                {buttonTxt}{isSubmitting && "..."}
            </Button>
            <div>
                <p className='text-sm text-gray-500 mt-4'>
                    {method === 'sign-in' ? 'Don\'t have an account? ' : 'Already have an account? '}
                    <a
                        href={method === 'sign-in' ? `/auth/${role}/sign-up` : `/auth/${role}/sign-in`}
                        className='text-blue-600 hover:underline'>
                        {method === 'sign-in' ? 'Sign up now' : 'Sign in'}
                    </a>
                </p>
            </div>
        </div>
    )
}

export default FormFooter