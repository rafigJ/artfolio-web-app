import { Button, List, Result } from 'antd'
import React, { useEffect, useState } from 'react'
import PostService from '../../api/PostService'
import { useFetching } from '../../hooks/useFetching'
import type { Product } from '../../types/MockTypes/Product'
import PostCard from '../PostCard/PostCard'

const PostGrid = () => {
	const [data, setData] = useState<Product[]>([])
	
	const [fetchPosts, isLoading, isError, error] = useFetching(async () => {
		const response = await PostService.getProducts()
		setData(response.data)
	})
	
	useEffect(() => {
		fetchPosts()
	}, [])
	
	if (isError) {
		return (
			<Result
				status='warning'
				title={'There are some problems with your operation. ' + error}
				extra={
					<Button type='primary' key='console'>
						Go Console
					</Button>
				}
			/>
		)
	}
	
	return (
		<List
			loading={isLoading}
			grid={{
				gutter: 16,
				xs: 1,
				sm: 2,
				md: 4,
				lg: 4,
				xl: 4,
				xxl: 4
			}}
			dataSource={data}
			renderItem={(item) => (
				<List.Item>
					<PostCard key={item.id} product={item} />
				</List.Item>
			)}
		/>
	)
}

export default PostGrid