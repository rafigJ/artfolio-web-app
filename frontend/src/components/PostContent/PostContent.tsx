import {
	AntDesignOutlined,
	DeleteOutlined,
	EditOutlined,
	EllipsisOutlined,
	FlagFilled,
	HeartFilled,
} from '@ant-design/icons'
import {
	Avatar,
	Button,
	Divider,
	Dropdown,
	Flex,
	MenuProps,
	Typography
} from 'antd'
import { useEffect, useState, type CSSProperties, type FC } from 'react'
import { Link, useParams } from 'react-router-dom'
import type {
	MockFullPostResponse,
	MockOwner,
} from '../../types/MockTypes/MockFullPostResponse'
import ReportWindow from '../ReportWindow/ReportWindow'
import './PostContent.css'

interface AuthorLinkCardProps {
	owner: MockOwner
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
						{owner.fullName}
					</Typography.Title>
				</Link>

				<Typography.Text>Воронеж, Россия</Typography.Text>
			</div>
		</div>
	)
}

const PostContent = () => {
	const [open, setOpen] = useState(false)

	const showModal = () => {
		setOpen(true)
	}


	const items: MenuProps['items'] = [
		{
			key: '1',
			label: <Link to={'/posts/create'}>Редактировать</Link>,
			icon: <EditOutlined />,
		},
		{
			key: '2',
			label: 'Удалить',
			icon: <DeleteOutlined />,
		},
		{
			key: '3',
			label: 'Пожаловаться',
			onClick: showModal,
			icon: <FlagFilled color='red' />,
			danger: true,
		},
	]
	const params = useParams()
	const [post, setPost] = useState<MockFullPostResponse>({
		id: 33,
		title: 'Sternwarte Darmstadt HDR Panorama 10MB',
		description:
			'Dieses Bild zeigt den Ausblick von der Sternwarte in Richtung Darmstadt, ' +
			'Frankfurt ist ebenfalls hinter Darmstadt zu sehen. Das Bild ist ein HDR (High-Dynamic-Range) Panorama, und setzt sich aus mehr als 20 Einzelbildern zusammen. Die Farbgebung ist künstlerisch ausgearbeitet, alle Details sind allerdings zu 100% der Realität entsprechend, eine Komposition oder Retusche hat nicht stattgefunden.',
		likeCount: 39,
		previewMedia: 949,
		medias: [
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg',
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg',
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg',
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg',
		],
		owner: {
			fullName: 'James Breitenstein',
			username: 'james54',
		},
	})


	useEffect(() => console.log(params.id), [])
	return (
		<>
			<ReportWindow open={open} setOpen={setOpen} />
			<div className='title-container'>
				<Typography.Title level={3} className='title'>
					{post.title}
				</Typography.Title>
				<Dropdown menu={{ items }} placement='bottomLeft' arrow>
					<Button className='menu-btn'>
						<EllipsisOutlined />
					</Button>
				</Dropdown>
			</div>
			{post.medias.map((media, index) => (
				<div key={index}>
					<img
						src={media}
						alt={index.toString()}
						style={{ marginTop: '10px', maxWidth: '100%' }}
					/>
				</div>
			))}
			<Flex justify='space-between' style={{ marginTop: 13 }}>
				<AuthorLinkCard
					owner={post.owner}
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
				<Typography.Text>{post.description}</Typography.Text>
			</div>
		</>
	)
}

export default PostContent
