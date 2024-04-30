import { Button, List, Result, Skeleton } from 'antd'
import { useEffect, useState } from 'react'
import InfiniteScroll from 'react-infinite-scroll-component'
import PostService from '../../api/PostService'
import { useFetching } from '../../hooks/useFetching'
import { FeedSection } from '../../types/MockTypes/MockFeedRequest'
import type { Product } from '../../types/MockTypes/Product'
import NewTabs from '../NewTabs/NewTabs'
import PostCard from '../PostCard/PostCard'

const PostGrid = () => {
	const [page, setPage] = useState(1)
	const [data, setData] = useState<Product[]>([])
	const [activeTabKey, setActiveTabKey] = useState<string>(FeedSection.NEW)

	const [fetchPosts, isLoading, isError, error] = useFetching(async _page => {
		const response = await PostService.getProducts(_page, 20)
		setData([...data, ...response.data])
	})

	useEffect(() => {
		// fetchPosts(page) // todo убрать
	}, [page, activeTabKey])

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
		<>
			<NewTabs
				onChange={key => {
					setData([])
					setPage(1)
					console.log(key)
					setActiveTabKey(key)
				}}
				label={['Новые', 'Популярные', 'Для вас']}
			/>
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
						md: 3,
						lg: 4,
						xl: 4,
						xxl: 4,
					}}
					dataSource={data}
					renderItem={item => (
						<List.Item>
							<PostCard key={item.id} product={item} />
						</List.Item>
					)}
				/>
			</InfiniteScroll>
		</>
	)
}

export default PostGrid
