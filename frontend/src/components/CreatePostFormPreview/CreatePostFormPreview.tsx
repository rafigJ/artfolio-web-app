import React, { type FC } from 'react'
import type { Product } from '../../types/MockTypes/Product'
import PostCard from '../PostCard/PostCard'

interface CreatePostFormPreviewProps {
	post: Product;
}

const CreatePostFormPreview: FC<CreatePostFormPreviewProps> = ({post}) => {
	return (
		<div>
			<PostCard product={post}/>
		</div>
	)
}

export default CreatePostFormPreview