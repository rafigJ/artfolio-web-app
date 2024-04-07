import { Flex } from 'antd'
import React from 'react'
import CommentList from '../../components/CommentList/CommentList'
import PostContent from '../../components/PostContent/PostContent'

const PostPage = () => {
	return (
		<main style={{ display: 'flex', justifyContent: 'center' }}>
			<Flex vertical style={{ maxWidth: '80%' }}>
				<PostContent />
				<CommentList />
			</Flex>
		</main>
	)
}

export default PostPage