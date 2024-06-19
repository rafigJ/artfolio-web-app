import { UserResponse } from '../UserResponse'

export interface PostResponse {
	id: number
	name: string
	description: string
	likeCount: number
	previewMediaId: number
	owner: UserResponse
}
