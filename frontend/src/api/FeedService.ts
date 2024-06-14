import type { AxiosResponse } from 'axios'
import type { IPageResponse } from '../types/IPageResponse'
import type { PostResponse } from '../types/post/PostResponse'
import $api from './index'

export default class FeedService {
	
	static async getFeed(section: 'NEW' | 'POPULAR' | 'SUBSCRIBE' | null,
	                     page: number = 0,
	                     limit: number = 10): Promise<AxiosResponse<IPageResponse<PostResponse>>> {
		return await $api.get('/feed', {
				params: {
					_page: page,
					_limit: limit,
					section: section
				}
			}
		)
	}
	
	static async searchFeed(name: string,
	                        page: number = 0,
	                        limit: number = 10): Promise<AxiosResponse<IPageResponse<PostResponse>>> {
		return await $api.get('/feed/search', {
				params: {
					_page: page,
					_limit: limit,
					name: name
				}
			}
		)
	}
	
}
