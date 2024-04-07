import { Typography } from 'antd'
import React, { type FC } from 'react'
import type { MockFullPost } from '../../types/MockTypes/MockFullPost'

interface CreatePostFormPreviewProps {
	post: MockFullPost;
}

const CreatePostFormPreview: FC<CreatePostFormPreviewProps> = ({ post }) => {
	return (
		<div style={{ display: 'block', flexDirection: 'column' }}>
			{/* <PostCard product={post} /> */}
			<Typography.Title level={3}>
				{post.title}
			</Typography.Title>
			
			<Typography.Text>
				{post.description}
			</Typography.Text>
		</div>
	)
}

export default CreatePostFormPreview