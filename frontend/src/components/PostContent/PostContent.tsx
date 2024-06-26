import {
	AntDesignOutlined,
	DeleteOutlined,
	EditOutlined,
	EllipsisOutlined,
	FlagFilled,
	HeartFilled,
	HeartOutlined
} from '@ant-design/icons'
import { Avatar, Button, Divider, Dropdown, Flex, MenuProps, Skeleton, Typography, message } from 'antd'
import { useContext, useEffect, useState, type CSSProperties, type FC } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import $api, { API_URL } from '../../api'
import PostService from '../../api/PostService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import { UserResponse } from '../../types/UserResponse'
import type { FullPostResponse } from '../../types/post/FullPostResponse'
import Error404Result from '../Error404Result/Error404Result'
import ReportPostWindow from '../ReportWindow/ReportPostWindow'
import './PostContent.css'

interface AuthorLinkCardProps {
	owner: UserResponse
	style: CSSProperties
}

const AuthorLinkCard: FC<AuthorLinkCardProps> = ({ owner, style }) => {
	const place = [owner?.city, owner?.country].filter(p => p).join(', ')
	return (
		<div style={style}>
			<Link to={`/profile/${owner?.username}`}>
				<Avatar
					src={`${API_URL}/user/${owner?.username}/avatar`}
					size={70}
					style={{ marginRight: '10px' }}
					icon={<AntDesignOutlined />}
				/>
			</Link>

			<div style={{ display: 'flex', flexDirection: 'column' }}>
				<Link to={`/profile/${owner?.username}`}>
					<Typography.Title style={{ marginTop: '15px' }} level={4}>
						{owner?.fullName}
					</Typography.Title>
				</Link>

				<Typography.Text>{place}</Typography.Text>
			</div>
		</div>
	)
}

const PostContent = () => {
	const [openPostReport, setOpenPostReport] = useState(false)
	const showPostReport = () => {
		setOpenPostReport(true)
	}
	const [isLiked, setIsLiked] = useState(false)
	const [likesCount, setLikesCount] = useState(0)
	const { authCredential, isAuth } = useContext(AuthContext)
	const navigate = useNavigate()


	const handleLike = async () => {
		if (!isAuth) {
			navigate('/login')
			return
		}
		try {
			const response = await $api.post(`posts/${post.id}/like`)
			if (response.status === 200) {
				setIsLiked(true)
				setLikesCount(response.data.likeCount)
			}
		} catch (error) {
			message.error('Произошла ошибка при лайке поста')
		}
	}

	const handleUnlike = async () => {
		if (!isAuth) {
			navigate('/login')
			return
		}
		try {
			const response = await $api.delete(`posts/${post.id}/like`)
			if (response.status === 200) {
				setIsLiked(false)
				setLikesCount(response.data.likeCount)
			}
		} catch (error) {
			message.error('Произошла ошибка при лайке поста')
		}
	}
	const params = useParams()

	const [post, setPost] = useState<FullPostResponse>({} as FullPostResponse)

	const [fetchPost, isLoading, isError] = useFetching(async (id) => {
		const response = await PostService.getPostById(id)
		setPost(response.data)
		setLikesCount(response.data.likeCount)
		setIsLiked(response.data.hasLike === null ? false : response.data.hasLike)
	})

	const [deletePost] = useFetching(async (id) => {
		PostService.deletePost(id)
			.then(() => {
				message.success("Публикация успешно удалена")
				navigate('/login')
			}
			)
			.catch(e => {
				message.error(`Ошибка удаления публикации ${e}`)
			})
	})

	const getMenuItems = () => {
		const items: MenuProps['items'] = [
			{
				key: '3',
				label: 'Пожаловаться',
				onClick: () => { isAuth ? showPostReport() : navigate('/login') },
				icon: <FlagFilled color='red' />,
				danger: true
			}
		]

		if (isAuth && post.owner && (authCredential.role === 'ADMIN' || authCredential.username === post.owner.username)) {
			items.unshift({
				key: '2',
				label: 'Удалить',
				icon: <DeleteOutlined />,
				onClick: () => deletePost(post.id)
			})
		}
		if (isAuth && post.owner && (authCredential.username === post.owner.username)) {
			items.unshift({
				key: '1',
				label: <Link to={`/posts/edit/${params.id}`}>Редактировать</Link>,
				icon: <EditOutlined />
			})
		}
		return items
	}

	useEffect(() => {
		fetchPost(params.id)
	}, [params.id])

	if (isLoading) {
		return <PostLoadingContent />
	}

	if (isError) {
		return (
			<Error404Result />
		)
	}

	return (
		<>
			<ReportPostWindow open={openPostReport} setOpen={setOpenPostReport} />
			<div className='title-container'>
				<Typography.Title level={3} className='title'>
					{post?.name}
				</Typography.Title>
				<Dropdown menu={{ items: getMenuItems() }} placement='bottomLeft' arrow>
					<Button className='menu-btn'>
						<EllipsisOutlined />
					</Button>
				</Dropdown>
			</div>
			{post?.mediaIds?.map((media, index) => (
				<div key={index}>
					<img
						src={`${API_URL}/posts/medias/${media}`}
						alt={index.toString()}
						style={{ marginTop: '10px', maxWidth: '100%' }}
					/>
				</div>
			))}
			<Flex justify='space-between' style={{ marginTop: 13, minWidth: '600px' }}>
				<AuthorLinkCard
					owner={post?.owner}
					style={{ display: 'flex', minWidth: '30%' }}
				/>
				<div style={{ display: 'flex', alignItems: 'center' }}>
					<Typography.Title
						level={3}
						style={{ margin: '0 5px 0 0', padding: 0 }}
					>
						{likesCount}
					</Typography.Title>
					{isLiked ? (
						<HeartFilled style={{ fontSize: '28px', color: 'red' }} onClick={handleUnlike} />
					) : (
						<HeartOutlined style={{ fontSize: '28px', color: 'red' }} onClick={handleLike} />
					)}
				</div>
			</Flex>
			<div>
				<Divider>Описание</Divider>
				<Typography.Text>{post?.description}</Typography.Text>
			</div>
		</>
	)
}

const PostLoadingContent = () => {
	return (
		<div style={{ minWidth: '600px' }}>
			<div className='title-container'>
				<Skeleton active />
				<Skeleton active />
			</div>
			<div>
				<Skeleton active />
			</div>
			<Flex justify='space-between' style={{ marginTop: 13 }}>
				<Skeleton active />
			</Flex>
			<div>
				<Skeleton active />
			</div>
		</div>
	)
}

export default PostContent
