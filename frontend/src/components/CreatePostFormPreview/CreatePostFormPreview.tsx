import { Typography } from 'antd'
import React, { type FC } from 'react'
import type { Product } from '../../types/MockTypes/Product'
import PostCard from '../PostCard/PostCard'

interface CreatePostFormPreviewProps {
	post: Product;
}

const CreatePostFormPreview: FC<CreatePostFormPreviewProps> = ({ post }) => {
	return (
		<div style={{ display: 'block', flexDirection: 'column' }}>
			{/* <PostCard product={post} /> */}
			<Typography.Title level={3}>
				{post.title}
			</Typography.Title>
		</div>
	)
}

export default CreatePostFormPreview