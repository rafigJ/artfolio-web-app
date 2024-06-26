import { AxiosResponse } from 'axios'
import { IPageResponse } from '../types/IPageResponse'
import { PostResponse } from '../types/post/PostResponse'
import { EditProfileRequest } from '../types/user/EditProfileRequest'
import type { FullUserResponse } from '../types/user/FullUserResponse'
import $api from './index'

export default class UserService {

	static async getUserByUsername(username: string): Promise<AxiosResponse<FullUserResponse>> {
		return await $api.get<FullUserResponse>(`/user/${username}`)
	}

	static async editUserProfile(request: EditProfileRequest, avatar: File | any): Promise<AxiosResponse<FullUserResponse>> {
		const bodyFormData = new FormData()

		bodyFormData.append('userInfo', new Blob([JSON.stringify(request)], { type: 'application/json' }))
		bodyFormData.append('avatarFile', avatar)
		return await $api.put('/user', bodyFormData)
	}

	static async getPostsByUsername(username: string): Promise<AxiosResponse<IPageResponse<PostResponse>>> {
		return await $api.get<IPageResponse<PostResponse>>(`/user/${username}/posts`)
	}

	static async deleteUser(username: string): Promise<AxiosResponse<void>> {
		return await $api.delete(`user/${username}`)
	}

}