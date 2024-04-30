export interface FullPostResponse {
	id: number
	name: string
	description: string
	likeCount: number
	previewMedia: number
	mediaIds: number[]
	owner: Owner
}

export interface Owner {
	fullName: string
	username: string
}
