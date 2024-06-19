import { UserResponse } from '../UserResponse'

export interface FullPostResponse {
	id: number
	name: string
	description: string
	likeCount: number
	mediaIds: number[]
	hasLike: false,
	owner: UserResponse
}
