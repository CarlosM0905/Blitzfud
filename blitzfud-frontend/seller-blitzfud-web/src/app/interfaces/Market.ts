export interface Market{
    name: string;
    description: string;
    address?: string;
    latitude?: number;
    longitude?: number;
    pickup?: boolean;
    delivery?: boolean;
    deliveryPrice?: number;
}
