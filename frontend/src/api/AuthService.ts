import { AxiosResponse } from 'axios'
import { AuthResponse } from '../types/auth/AuthResponse'
import type { RegistrationRequest } from '../types/auth/RegistrationRequest'
import $api from './index'

export default class AuthService {
	
	static async login(email: string, password: string): Promise<AxiosResponse<AuthResponse>> {
		return await $api.post<AuthResponse>('/auth/login', { email, password })
	}
	
	static async register(request: RegistrationRequest, avatar: File | any): Promise<AxiosResponse<AuthResponse>> {
		const bodyFormData = new FormData()
		
		bodyFormData.append('userInfo', new Blob([JSON.stringify(request)], { type: 'application/json' }))
		if (avatar) {
			bodyFormData.append('avatarFile', avatar)
		}
		return await $api.post<AuthResponse>('/auth/register', bodyFormData)
	}
	
	static async userCredentials(): Promise<AxiosResponse<AuthResponse>> {
		return await $api.get('/auth')
	}
};