export interface AuthResponse {
	username: string;
	email: string;
	name: string;
	role: 'USER' | 'ADMIN';
	token: string;
}
