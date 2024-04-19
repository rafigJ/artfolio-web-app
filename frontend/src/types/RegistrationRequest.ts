import type { UploadFile } from 'antd'

export interface RegistrationRequest {
	username: string
	email: string
	password: string
	secretWord: string
	fullName: string
	country: string
	city: string
	description: string
	avatarFile: UploadFile
}