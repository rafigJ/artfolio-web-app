import { List, Result, Skeleton } from 'antd'
import { useContext, useEffect, useState } from 'react'
import InfiniteScroll from 'react-infinite-scroll-component'
import { useNavigate } from 'react-router-dom'
import FeedService from '../../api/FeedService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import { FEED_VALUES, FeedSection } from '../../types/MockTypes/MockFeedRequest'
import type { PostResponse } from '../../types/PostResponse'
import NewTabs from '../NewTabs/NewTabs'
import PostCard from '../PostCard/PostCard'

const PostGrid = () => {
	const { isAuth } = useContext(AuthContext)
	const navigate = useNavigate()
	const [page, setPage] = useState(0)
	const [data, setData] = useState<PostResponse[]>([])
	const [totalElements, setTotalElements] = useState(0)
	const [activeTabKey, setActiveTabKey] = useState<FeedSection | null>(FeedSection.NEW)
	
	const [fetchPosts, isLoading, isError, error] = useFetching(async (section: FeedSection | null, _page) => {
		const response = await FeedService.getFeed(section, page, 20)
		setData([...data, ...response.data.content])
		setTotalElements(response.data.totalElements)
	})
	
	useEffect(() => {
		fetchPosts(activeTabKey, page)
	}, [page, activeTabKey])
	
	if (isError) {
		return (
			<Result
				status='warning'
				title={'There are some problems with your operation. ' + error}
			/>
		)
	}
	
	return (
		<>
			<NewTabs
				onChange={activeKey => {
					setData([])
					setPage(0)
					setTotalElements(0)
					if (!isAuth && FEED_VALUES[Number(activeKey)] === FeedSection.SUBSCRIBE) {
						navigate('/login')
					}
					setActiveTabKey(FEED_VALUES[Number(activeKey)])
				}}
				label={['Новые', 'Популярные', 'Для вас']}
			/>
			<InfiniteScroll
				style={{ overflowX: 'hidden' }}
				dataLength={data.length}
				next={() => setPage(page + 1)}
				hasMore={data.length < totalElements}
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
						xxl: 4
					}}
					dataSource={data}
					renderItem={item => (
						<List.Item>
							<PostCard key={item.id} post={item} />
						</List.Item>
					)}
				/>
			</InfiniteScroll>
		</>
	)
}

export default PostGrid
