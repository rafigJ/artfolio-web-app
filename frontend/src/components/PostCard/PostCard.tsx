import { HeartFilled } from '@ant-design/icons'
import { Avatar, Card, Typography } from 'antd'
import React, { type FC } from 'react'
import { useNavigate } from 'react-router-dom'
import type { Product } from '../../types/MockTypes/Product'

const { Meta } = Card

interface PostCardProps {
	product: Product
}

const PostCard: FC<PostCardProps> = ({ product }) => {
	const navigate = useNavigate()
	
	return (
		<Card
			hoverable
			style={{ maxWidth: 406 }}
			cover={
				<img
					style={{ maxWidth: 406, maxHeight: 204, objectFit: 'cover' }}
					alt={product.title}
					src={product.url} />
			}
			onClick={() => navigate(`/posts/${product.id}`)}
		>
			<Meta
				title={product.title}
				avatar={<Avatar src={`https://api.dicebear.com/7.x/miniavs/svg?seed=${product.id}`} />}
				description={'Автор публикации'}
			/>
			<div style={{ height: '30px', display: 'flex', position: 'absolute', bottom: 10, right: 10 }}>
				<Typography.Title level={5}
				                  style={{ fontSize: 24, margin: '0px 10px 30px 0px' }}>{product.id}</Typography.Title>
				<HeartFilled style={{ fontSize: '24px', color: 'red', marginTop: '7px' }} />
			</div>
		
		</Card>
	)
}

export default PostCard