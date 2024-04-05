export interface IPageResponse<T>{
	content: T[];
	totalElements: number;
	totalPages: number;
}