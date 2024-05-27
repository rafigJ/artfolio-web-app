import { AntDesignOutlined } from '@ant-design/icons'
import { Avatar, Button, Modal, Table, message } from 'antd'
import { FC, useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import $api, { API_URL } from '../../api'
import { AuthContext } from '../../context'
import { FullUserResponse } from '../../types/FullUserResponse'

interface SubscriptionsWindowProps {
	user: FullUserResponse
	open: boolean
	setOpen: (state: boolean) => void
}

interface Subscribes {
	uuid: string
	fullName: string
	username: string
	email: string
	isSubscribed: boolean
}

const SubscriptionWindow: FC<SubscriptionsWindowProps> = ({ user, open, setOpen }) => {
	const [subscribtions, setSubscribtions] = useState<Subscribes[]>([])

	useEffect(() => {
		if (open) {
			handleSubscribtions()
		}
	}, [open])

	const { authCredential } = useContext(AuthContext)

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
		{
			title: '',
			dataIndex: 'isSubscribed',
			key: 'isSubscribed',
			render: (isSubscribed: boolean, record: Subscribes) => (
				<span>
					<Button
						style={{ margin: '10px 0' }}
						danger={isSubscribed}
						type='primary'
						onClick={() => handleSubscribeToggle(record.username, isSubscribed)}
					>
						{isSubscribed ? 'Отписаться' : 'Подписаться'}
					</Button>
				</span>
			),
		},
	]
	if (authCredential.username != user.username) {
		columns.pop()
	}

	const handleCancel = () => {
		setOpen(false)
	}

	const handleSubscribtions = async () => {
		try {
			const response = await $api.get(`/user/${user.username}/subscribes`)
			if (response.status === 200) {
				// Извлечение и преобразование данных из ответа
				const subscribtionData = response.data.content.map((subscribtion: any) => ({
					...subscribtion,
					isSubscribed: true, // Предположительно, все пользователи в списке подписок изначально подписаны
					key: subscribtion.uuid, // Используем uuid как ключ
				}))
				setSubscribtions(subscribtionData)
			}
		} catch (error) {
			message.error('Произошла ошибка при получении списка подписчиков')
		}
	}

	const handleSubscribeToggle = async (username: string, isSubscribed: boolean) => {
		try {
			if (isSubscribed) {
				await $api.delete(`/user/${username}/subscribes`)
				message.success(`Вы отписались от пользователя ${username}`)
			} else {
				await $api.post(`/user/${username}/subscribes`)
				message.success(`Вы подписались на пользователя ${username}`)
			}
			// Обновить состояние подписки
			setSubscribtions((prev) =>
				prev.map((subscribtion) =>
					subscribtion.username === username
						? { ...subscribtion, isSubscribed: !isSubscribed }
						: subscribtion
				)
			)
		} catch (error) {
			message.error('Произошла ошибка при изменении подписки')
		}
	}

	return (
		<Modal
			width={600}
			title="Список подписок"
			open={open}
			onCancel={handleCancel}
			footer={null}
		>
			<Table columns={columns} dataSource={subscribtions} />
		</Modal>
	)
}

export default SubscriptionWindow
