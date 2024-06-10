import type { AxiosResponse } from 'axios'
import type { FullPostResponse } from '../types/FullPostResponse'
import type { Product } from '../types/MockTypes/Product'
import type { PostRequest } from '../types/PostRequest'
import type { PostResponse } from '../types/PostResponse'
import $api from './index'

export default class PostService {
	static async getProducts(
		page: number = 0,
		limit: number = 10
	): Promise<AxiosResponse<Product[]>> {
		// TODO Удалить
		return await $api.get('/photos', {
			params: {
				_page: page,
				_limit: limit,
			},
		})
	}
	
	static async createPost(post: PostRequest, files: Blob[]): Promise<AxiosResponse<PostResponse>> {
		const bodyFormData = new FormData();
		bodyFormData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }))
		files.forEach(file => {
			bodyFormData.append('file', file);
		});
		return await $api.post<PostResponse>('/posts', bodyFormData)
	}
	
	static async editPost(postId: number, post: PostRequest, files: Blob[]): Promise<AxiosResponse<PostResponse>> {
		const bodyFormData = new FormData();
		bodyFormData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }))
		files.forEach(file => {
			bodyFormData.append('file', file);
		});
		return await $api.put<PostResponse>(`/posts/${postId}`, bodyFormData)
	}
	
	static async getPostById(postId: number): Promise<AxiosResponse<FullPostResponse>> {
		return $api.get(`/posts/${postId}`)
	}
}
