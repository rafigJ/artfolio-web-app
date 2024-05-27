import { Button, Modal, Table, message } from 'antd'
import { FC, useEffect, useState } from 'react'
import $api from '../../api'
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
			title: 'Полное имя',
			dataIndex: 'fullName',
			key: 'fullName',
		},
		{
			title: 'Имя пользователя',
			dataIndex: 'username',
			key: 'username',
		},
		{
			title: 'Электронная почта',
			dataIndex: 'email',
			key: 'email',
		},
		{
			title: '',
			dataIndex: 'isSubscribed',
			key: 'isSubscribed',
			render: (isSubscribed: boolean, record: Subscriber) => (
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
					isSubscribed: true, // Предположительно, все пользователи в списке подписок изначально подписаны
					key: subscriber.uuid, // Используем uuid как ключ
				}))
				setSubscribers(subscribersData)
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
			setSubscribers((prev) =>
				prev.map((subscriber) =>
					subscriber.username === username
						? { ...subscriber, isSubscribed: !isSubscribed }
						: subscriber
				)
			)
		} catch (error) {
			message.error('Произошла ошибка при изменении подписки')
		}
	}

	return (
		<Modal
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
