export interface AuthResponse {
	email: string;
	name: string;
	role: 'USER' | 'ADMIN';
	token: string;
}
