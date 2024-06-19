import type { AxiosResponse } from 'axios'
import type { FullPostResponse } from '../types/post/FullPostResponse'
import type { PostRequest } from '../types/post/PostRequest'
import type { PostResponse } from '../types/post/PostResponse'
import $api from './index'

export default class PostService {

	static async createPost(post: PostRequest, files: Blob[]): Promise<AxiosResponse<PostResponse>> {
		const bodyFormData = new FormData()
		bodyFormData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }))
		files.forEach(file => {
			bodyFormData.append('file', file)
		})
		return await $api.post<PostResponse>('/posts', bodyFormData)
	}

	static async editPost(postId: number, post: PostRequest, files: Blob[]): Promise<AxiosResponse<PostResponse>> {
		const bodyFormData = new FormData()
		bodyFormData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }))
		files.forEach(file => {
			bodyFormData.append('file', file)
		})
		return await $api.put<PostResponse>(`/posts/${postId}`, bodyFormData)
	}

	static async getPostById(postId: number): Promise<AxiosResponse<FullPostResponse>> {
		return $api.get(`/posts/${postId}`)
	}

	static async deletePost(postId: number): Promise<AxiosResponse<void>> {
		return await $api.delete(`/posts/${postId}`)
	}
}
