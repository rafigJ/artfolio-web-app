import { Button, List, Result, Skeleton } from 'antd'
import React, { useEffect, useState } from 'react'
import InfiniteScroll from 'react-infinite-scroll-component'
import PostService from '../../api/PostService'
import { useFetching } from '../../hooks/useFetching'
import type { Product } from '../../types/MockTypes/Product'
import PostCard from '../PostCard/PostCard'
import NewTabs from '../Tabs/NewTabs'

const PostGrid = () => {
	const [page, setPage] = useState(0)
	const [data, setData] = useState<Product[]>([])
	
	const [fetchPosts, isLoading, isError, error] = useFetching(async (_page) => {
		const response = await PostService.getProducts(_page, 20)
		setData([...data, ...response.data])
	})
	
	useEffect(() => {
		fetchPosts(page)
	}, [page])
	
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
		<InfiniteScroll
			style={{ overflowX: 'hidden' }}
			dataLength={data.length}
			next={() => setPage(page + 1)}
			hasMore={data.length < 100}
			loader={<Skeleton avatar paragraph={{ rows: 1 }} active />}
		>
			
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
		</InfiniteScroll>
	)
}

export default PostGrid