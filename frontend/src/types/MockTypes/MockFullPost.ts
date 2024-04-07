export interface MockFullPost {
	id: number
	name: string
	description: string
	likeCount: number
	previewMedia: number
	medias: number[]
	owner: MockOwner
}

export interface MockOwner {
	fullName: string
	username: string
}
