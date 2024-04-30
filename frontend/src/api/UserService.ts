import { AxiosResponse } from 'axios'
import type { FullUserResponse } from '../types/FullUserResponse'
import $api from './index'

export default class UserService {
	
	static async getUserByUsername(username: string): Promise<AxiosResponse<FullUserResponse>> {
		return await $api.get<FullUserResponse>(`/user/${username}`)
	}
	
};