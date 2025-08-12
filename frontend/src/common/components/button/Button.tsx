import React, { type ButtonHTMLAttributes } from 'react'

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    onClick?: (event: React.MouseEvent<HTMLButtonElement>) => void;
    children: React.ReactNode;
    type?: 'button' | 'submit' | 'reset';
    className?: string;
    disabled?: boolean;
}

function Button({ onClick, children, type, className = '', disabled = false }: ButtonProps) {
    return (
        <button
            type={type}
            onClick={onClick}
            className={className}
            disabled={disabled}
        >
            {children}
        </button>
    );
}

export default Button