import { HeartFilled } from '@ant-design/icons'
import { Avatar, Card, Typography } from 'antd'
import { type FC } from 'react'
import { Link, useNavigate } from 'react-router-dom'
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
			style={{ maxWidth: 406, cursor: 'default' }}
			cover={
				<img
					style={{
						maxWidth: 406,
						maxHeight: 204,
						objectFit: 'cover',
						cursor: 'pointer',
					}}
					alt={product.title}
					src={product.url}
					onClick={() => navigate(`/posts/${product.id}`)}
				/>
			}
		>
			<Meta
				style={{ cursor: 'default' }}
				title={
					<Link className='ant-card-meta-title' to={`/posts/${product.id}`}>
						{product.title}
					</Link>
				}
				avatar={
					<Link to='/profile/username'>
						<Avatar
							src={`https://api.dicebear.com/7.x/miniavs/svg?seed=${product.id}`}
						/>
					</Link>
				}
				description={
					<Link className='ant-card-meta-description' to='/profile/username'>
						Автор публикации
					</Link>
				}
			/>
			<div
				style={{
					height: '30px',
					display: 'flex',
					position: 'absolute',
					bottom: 10,
					right: 10,
				}}
			>
				<Typography.Title
					level={5}
					style={{ fontSize: 24, margin: '0px 10px 30px 0px' }}
				>
					{product.id}
				</Typography.Title>
				<HeartFilled
					style={{ fontSize: '24px', color: 'red', marginTop: '7px' }}
				/>
			</div>
		</Card>
	)
}

export default PostCard
