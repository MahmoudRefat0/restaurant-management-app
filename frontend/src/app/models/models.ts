export type Role = 'ADMIN' | 'STAFF';
export type TableStatus = 'AVAILABLE' | 'OCCUPIED' | 'RESERVED';
export type OrderStatus = 'PENDING' | 'PREPARING' | 'SERVED' | 'PAID' | 'CANCELLED';

export interface AuthResponse {
  token: string;
  username: string;
  fullName: string;
  role: Role;
}

export interface User {
  id: number;
  username: string;
  fullName: string;
  role: Role;
}

export interface UserRequest {
  username: string;
  password: string;
  fullName: string;
  role: Role;
}

export interface Category {
  id: number;
  name: string;
  description: string;
}

export interface CategoryRequest {
  name: string;
  description: string;
}

export interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
  categoryId: number;
  categoryName: string;
}

export interface MenuItemRequest {
  name: string;
  description: string;
  price: number;
  categoryId: number;
  available: boolean;
}

export interface RestaurantTable {
  id: number;
  number: number;
  capacity: number;
  status: TableStatus;
}

export interface TableRequest {
  number: number;
  capacity: number;
}

export interface OrderItem {
  menuItemId: number;
  name: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Order {
  id: number;
  tableNumber: number;
  status: OrderStatus;
  totalAmount: number;
  createdBy: string;
  createdAt: string;
  items: OrderItem[];
}

export interface OrderRequest {
  tableId: number;
  items: { menuItemId: number; quantity: number }[];
}
