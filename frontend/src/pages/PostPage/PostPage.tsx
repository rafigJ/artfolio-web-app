import { Flex } from 'antd'
import React from 'react'
import CommentEditor from '../../components/CommentEditor/CommentEditor'
import PostContent from '../../components/PostContent/PostContent'

const PostPage = () => {
	return (
		<main style={{ display: 'flex', justifyContent: 'center' }}>
			<Flex vertical style={{ maxWidth: '80%' }}>
				<PostContent />
				<CommentEditor />
			</Flex>
		</main>
	)
}

export default PostPage