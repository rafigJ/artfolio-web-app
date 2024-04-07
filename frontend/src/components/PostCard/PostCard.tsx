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
			style={{ maxWidth: 406 }}
			cover={
				<div style={{ maxWidth: 406, maxHeight: 204, overflow: 'hidden' }}>
					<img
						style={{ width: '100%', height: 'auto', objectFit: 'cover' }}
						alt={product.title}
						src={product.url}
					/>
				</div>
			}
		>
			<Meta title={product.title} />
		</Card>
	)
}

export default PostCard