import type { AxiosResponse } from 'axios'
import type { Product } from '../types/MockTypes/Product'
import $api from './index'

export default class PostService {
	static async getProducts(page: number = 0, limit: number = 10): Promise<AxiosResponse<Product[]>> { // TODO Удалить
		return await $api.get('/photos', {
				params: {
					_page: page,
					_limit: limit
				}
			}
		)
	}
};