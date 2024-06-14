import { UserResponse } from '../UserResponse'

export interface CommentResponse {
	id: number
	owner: UserResponse
	comment: string
	createTime: string
}