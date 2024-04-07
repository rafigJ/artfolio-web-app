import { Avatar, Card } from 'antd'
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
			<Meta 
				title={product.title} 
				avatar={<Avatar src={`https://api.dicebear.com/7.x/miniavs/svg?seed=${product.id}`} />}
				description={"Автор публикации"}
			/>
		</Card>
	)
}

export default PostCard