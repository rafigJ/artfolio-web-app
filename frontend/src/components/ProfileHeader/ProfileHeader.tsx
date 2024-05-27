import { AntDesignOutlined } from '@ant-design/icons'
import { Avatar, Button, Flex, Typography, message } from 'antd'
import { useEffect, useState, type FC } from 'react'
import { API_URL } from '../../api'
import $api from '../../api/index'

import type { FullUserResponse } from '../../types/FullUserResponse'

interface ProfileHeaderProps {
	profile: FullUserResponse
}

const ProfileHeader: FC<ProfileHeaderProps> = ({ profile }) => {
	const [userIsSubscribed, setIsSubscribed] = useState(false)

	useEffect(() => {
		// Проверка начального состояния подписки (можно добавить запрос к API для получения статуса подписки)
	}, [])

	const handleSubscribe = async () => {
		try {
			const response = await $api.post(`/user/${profile.username}/subscribes`)
			if (response.status === 200) {
				setIsSubscribed(true)
			}
		} catch (error) {
			message.error('Произошла ошибка при подписке')
		}
	}

	const handleUnsubscribe = async () => {
		try {
			const response = await $api.delete(`/user/${profile.username}/subscribes`)
			if (response.status === 200) {
				setIsSubscribed(false)
			}
		} catch (error) {
			message.error('Произошла ошибка при отписке')
		}
	}

	return (
		<Flex
			style={{
				background: 'white',
				minHeight: 280,
				padding: 24,
				borderRadius: 8
			}}
			justify={'center'}
		>
			<Avatar
				src={`${API_URL}/user/${profile?.username}/avatar`}
				size={150}
				icon={<AntDesignOutlined />}
			/>
			<Flex vertical style={{ marginLeft: '15px' }}>
				<Typography.Title level={3}>{profile?.fullName}</Typography.Title>
				<Typography.Text>{`${profile?.city}, ${profile?.country}`}</Typography.Text>
				<Button
					style={{ margin: '10px 0' }}
					danger={userIsSubscribed}
					type='primary'
					onClick={userIsSubscribed ? handleUnsubscribe : handleSubscribe}
				>
					{userIsSubscribed ? 'Отписаться' : 'Подписаться'}
				</Button>
				<Button href={`mailto://${profile?.email}`}>Связаться</Button>
			</Flex>
		</Flex>
	)
}

export default ProfileHeader
