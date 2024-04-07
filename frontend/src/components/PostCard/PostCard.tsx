import { Card } from 'antd'
import React, { type FC } from 'react'
import type { Product } from '../../types/MockTypes/Product'

const { Meta } = Card

interface PostCardProps {
	product: Product
}

const PostCard: FC<PostCardProps> = ({ product }) => {
	return (
		<Card
			hoverable
			style={{ maxWidth: 406}}
			cover={
				<img
					style={{ maxWidth: 406, maxHeight: 204, objectFit: 'cover' }}
					alt='example'
					src={product.url} />
			}
		>
			<Meta title={product.title} />
		</Card>
	)
}

export default PostCard