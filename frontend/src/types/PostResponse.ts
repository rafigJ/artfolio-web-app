export interface PostResponse {
	id: number
	name: string
	description: string
	likeCount: number
	previewMediaId: number
	owner: Owner
}

export interface Owner {
	fullName: string
	username: string
}
