import { AntDesignOutlined } from '@ant-design/icons'
import { Avatar, Modal, Table, message } from 'antd'
import { FC, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import $api, { API_URL } from '../../api'
import { FullUserResponse } from '../../types/FullUserResponse'

interface SubscribersWindowProps {
	user: FullUserResponse
	open: boolean
	setOpen: (state: boolean) => void
}

interface Subscriber {
	uuid: string
	fullName: string
	username: string
	email: string
	isSubscribed: boolean
}

const SubscribersWindow: FC<SubscribersWindowProps> = ({ user, open, setOpen }) => {
	const [subscribers, setSubscribers] = useState<Subscriber[]>([])

	useEffect(() => {
		if (open) {
			handleSubscribers()
		}
	}, [open])

	const columns = [
		{
			dataIndex: 'username',
			key: 'avatar',
			render: (username: string) => (
				<Link to={`/profile/${username}`} onClick={() => setOpen(false)}>
					<Avatar src={`${API_URL}/user/${username}/avatar`}
						icon={<AntDesignOutlined />}
					/>
				</Link>
			),
		},
		{
			title: 'Имя пользователя',
			dataIndex: 'username',
			key: 'username',
			render: (username: string) => (
				<Link to={`/profile/${username}`} onClick={() => setOpen(false)}>
					{username}
				</Link>
			),
		},
		{
			title: 'Полное имя',
			dataIndex: 'fullName',
			key: 'fullName',
		},
	]

	const handleCancel = () => {
		setOpen(false)
	}

	const handleSubscribers = async () => {
		try {
			const response = await $api.get(`/user/${user.username}/followers`)
			if (response.status === 200) {
				// Извлечение и преобразование данных из ответа
				const subscribersData = response.data.content.map((subscriber: any) => ({
					...subscriber,
					key: subscriber.uuid, // Используем uuid как ключ
				}))
				setSubscribers(subscribersData)
			}
		} catch (error) {
			message.error('Произошла ошибка при получении списка подписчиков')
		}
	}

	return (
		<Modal
			width={600}
			title="Список подписчиков"
			open={open}
			onCancel={handleCancel}
			footer={null}
		>
			<Table columns={columns} dataSource={subscribers} />
		</Modal>
	)
}

export default SubscribersWindow
