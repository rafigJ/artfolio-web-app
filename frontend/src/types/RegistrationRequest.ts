export interface RegistrationRequest {
	username: string
	email: string
	password: string
	confirmPassword: string
	secretWord: string
	fullName: string
	country: string
	city: string
	profileDescription: string
}