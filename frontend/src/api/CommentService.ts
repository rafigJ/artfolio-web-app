import type { AxiosResponse } from 'axios'
import { CommentRequest } from '../types/comment/CommentRequest'
import { CommentResponse } from '../types/comment/CommentResponse'
import type { IPageResponse } from '../types/IPageResponse'
import $api from './index'

export default class CommentService {
	
	static async getComments(postId: number,
	                         page: number = 0,
	                         limit: number = 10): Promise<AxiosResponse<IPageResponse<CommentResponse>>> {
		return await $api.get(`/posts/${postId}/comments`, {
				params: {
					_page: page,
					_limit: limit
				}
			}
		)
	}
	
	static async createComment(postId: number,
	                           comment: CommentRequest): Promise<AxiosResponse<CommentResponse>> {
		return await $api.post(`/posts/${postId}/comments`, comment)
	}
	
	static async deleteComment(postId: number): Promise<AxiosResponse<void>> {
		return await $api.delete(`/posts/${postId}/comments`)
	}
}
