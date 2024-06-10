export interface FullPostResponse {
	id: number
	name: string
	description: string
	likeCount: number
	mediaIds: number[]
	hasLike: false,
	owner: Owner
}

export interface Owner {
	fullName: string
	username: string
}
