import { List, Result, Skeleton } from 'antd'
import React, { type FC, useEffect, useState } from 'react'
import InfiniteScroll from 'react-infinite-scroll-component'
import FeedService from '../../api/FeedService'
import { useFetching } from '../../hooks/useFetching'
import type { PostResponse } from '../../types/PostResponse'
import PostCard from '../PostCard/PostCard'

interface SearchPostGridProps {
	name: string
}

const SearchPostGrid: FC<SearchPostGridProps> = ({ name }) => {
	// для отслеживания состояния name (нужно для пагинации и обновления массива
	const [prevName, setPrevName] = useState('')
	const [page, setPage] = useState(0)
	const [data, setData] = useState<PostResponse[]>([])
	const [totalElements, setTotalElements] = useState(0)
	
	const [searchPosts, isLoading, isError, error] = useFetching(async (name, _page) => {
		const response = await FeedService.searchFeed(name, page, 20)
		if (prevName !== name) {
			setPage(0)
			setData([...response.data.content])
		} else {
			setData([...data, ...response.data.content])
		}
		setTotalElements(response.data.totalElements)
	})
	
	useEffect(() => {
		searchPosts(name, page)
		setPrevName(name)
	}, [name, page])
	
	if (isError) {
		return (
			<Result
				status='warning'
				title={'There are some problems with your operation. ' + error}
			/>
		)
	}
	return (
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
	)
}

export default SearchPostGrid