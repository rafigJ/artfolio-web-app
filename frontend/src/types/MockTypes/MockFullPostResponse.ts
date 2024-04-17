// То что мы получаем в ответ
export interface MockFullPostResponse {
	id: number
	title: string
	description: string
	likeCount: number
	previewMedia: number
	medias: string[]
	owner: MockOwner
}

export interface MockOwner {
	fullName: string
	username: string
}
