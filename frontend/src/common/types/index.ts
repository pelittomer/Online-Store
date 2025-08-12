export const Role = {
    Customer: "CUSTOMER",
    Seller: "SELLER",
    Admin: "ADMIN",
} as const
export type RoleType = (typeof Role)[keyof typeof Role]