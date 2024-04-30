import {
	AntDesignOutlined,
	DeleteOutlined,
	EditOutlined,
	EllipsisOutlined,
	FlagFilled,
	HeartFilled
} from '@ant-design/icons'
import { Avatar, Button, Divider, Dropdown, Flex, MenuProps, Result, Skeleton, Typography } from 'antd'
import { type CSSProperties, type FC, useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { API_URL } from '../../api'
import PostService from '../../api/PostService'
import { useFetching } from '../../hooks/useFetching'
import type { FullPostResponse, Owner } from '../../types/MockTypes/FullPostResponse'
import ReportWindow from '../ReportWindow/ReportWindow'
import './PostContent.css'

interface AuthorLinkCardProps {
	owner: Owner
	style: CSSProperties
}

const AuthorLinkCard: FC<AuthorLinkCardProps> = ({ owner, style }) => {
	return (
		<div style={style}>
			<Link to='/profile/username'>
				<Avatar
					src='https://api.dicebear.com/7.x/miniavs/svg?seed=10'
					size={70}
					icon={<AntDesignOutlined />}
				/>
			</Link>
			
			<div style={{ display: 'flex', flexDirection: 'column' }}>
				<Link to='/profile/username'>
					<Typography.Title style={{ marginTop: '15px' }} level={4}>
						{owner?.fullName}
					</Typography.Title>
				</Link>
				
				<Typography.Text>Воронеж, Россия</Typography.Text>
			</div>
		</div>
	)
}

const PostContent = () => {
	const [open, setOpen] = useState(false)
	const navigate = useNavigate()
	const showModal = () => {
		setOpen(true)
	}
	
	const items: MenuProps['items'] = [
		{
			key: '1',
			label: <Link to={'/posts/create'}>Редактировать</Link>,
			icon: <EditOutlined />
		},
		{
			key: '2',
			label: 'Удалить',
			icon: <DeleteOutlined />
		},
		{
			key: '3',
			label: 'Пожаловаться',
			onClick: showModal,
			icon: <FlagFilled color='red' />,
			danger: true
		}
	]
	
	const params = useParams()
	const [post, setPost] = useState<FullPostResponse>({} as FullPostResponse)
	
	const [fetchPost, isLoading, isError, error] = useFetching(async (id) => {
		const response = await PostService.getPostById(id)
		setPost(response.data)
	})
	
	useEffect(() => {
		fetchPost(params.id)
		console.log(isLoading)
	}, [])
	
	if (isLoading) {
		return <PostLoadingContent />
	}
	
	if (isError) {
		return (
			<Result status='404'
			        title='404'
			        subTitle='Страница не найдена'
			        extra={<Button title='Вернуться на главную' onClick={() => navigate('/')} />}
			/>
		)
	}

	return (
		<>
			<ReportWindow open={open} setOpen={setOpen} />
			<div className='title-container'>
				<Typography.Title level={3} className='title'>
					{post?.name}
				</Typography.Title>
				<Dropdown menu={{ items }} placement='bottomLeft' arrow>
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
						100
					</Typography.Title>
					<HeartFilled style={{ fontSize: '28px', color: 'red' }} />
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
		<div style={{minWidth: '600px'}}>
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
