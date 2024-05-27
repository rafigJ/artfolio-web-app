import { HeartFilled } from '@ant-design/icons'
import { Avatar, Card, Typography } from 'antd'
import { type FC } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import type { PostResponse } from '../../types/PostResponse'

const { Meta } = Card

interface PostCardProps {
	post: PostResponse
}

const PostCard: FC<PostCardProps> = ({ post }) => {
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
						cursor: 'pointer'
					}}
					alt={post.name}
					src={`${API_URL}/posts/${post.id}/preview`}
					onClick={() => navigate(`/posts/${post.id}`)}
				/>
			}
		>
			<Meta
				style={{ cursor: 'default' }}
				title={
					<Link className='ant-card-meta-title' to={`/posts/${post.id}`}>
						{post.name}
					</Link>
				}
				avatar={
					<Link to={`/profile/${post?.owner?.username}`}>
						<Avatar
							src={`${API_URL}/user/${post?.owner?.username}/avatar`}
						/>
					</Link>
				}
				description={
					<Link className='ant-card-meta-description' to={`/profile/${post?.owner?.username}`}>
						{post?.owner?.fullName}
					</Link>
				}
			/>
			<div
				style={{
					height: '30px',
					display: 'flex',
					position: 'absolute',
					bottom: 10,
					right: 10
				}}
			>
				<Typography.Title
					level={5}
					style={{ fontSize: 24, margin: '0px 10px 30px 0px' }}
				>
					{post.id}
				</Typography.Title>
				<HeartFilled
					style={{ fontSize: '24px', color: 'red', marginTop: '7px' }}
				/>
			</div>
		</Card>
	)
}

export default PostCard
