import { AntDesignOutlined } from '@ant-design/icons'
import { Avatar, Button, Flex, message, Typography } from 'antd'
import { useContext, useState, type FC } from 'react'

import { useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import $api from '../../api/index'
import UserService from '../../api/UserService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import type { FullUserResponse } from '../../types/user/FullUserResponse'

interface ProfileHeaderProps {
	profile: FullUserResponse
}

const ProfileHeader: FC<ProfileHeaderProps> = ({ profile }) => {
	const [userIsSubscribed, setIsSubscribed] = useState(profile?.isFollowed == null ? false : profile.isFollowed)
	const navigate = useNavigate()

	const { authCredential, isAuth } = useContext(AuthContext)

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

	const [deleteUser] = useFetching(async (username: string) => {
		await UserService.deleteUser(username)
			.then(() => {
				navigate('/login')
				message.success("Пользователь успешно удалён")
			})
			.catch(e => {
				message.error("Ошибка удаления пользователя" + e)
			})
	})

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
				<Typography.Text style={{ marginBottom: '5px' }}>
					{`${profile?.city}, 
					${profile?.country}`}
				</Typography.Text>
				{isAuth && authCredential.username == profile.username ? (
					<Button
						style={{ margin: '5px 0', minWidth: '200px' }}
						onClick={() => navigate('/profile/edit')}
					>
						Редактировать профиль
					</Button>
				) : (
					<Button
						style={{ margin: '5px 0', minWidth: '200px' }}
						danger={userIsSubscribed}
						type='primary'
						onClick={isAuth ? (userIsSubscribed ? handleUnsubscribe : handleSubscribe) : (() => navigate('/login'))}
					>
						{userIsSubscribed ? 'Отписаться' : 'Подписаться'}
					</Button>

				)}
				<Button
					href={`mailto://${profile?.email}`}
					onClick={() => window.ym(97163910, 'reachGoal', 'contactTo')}
					style={{ margin: '5px 0', minWidth: '200px' }}>
					Связаться
				</Button>
				{isAuth &&
					authCredential.role === 'ADMIN' &&
					authCredential.username != profile.username &&
					<Button
						style={{ margin: '5px 0', minWidth: '200px' }}
						danger={true}
						type='primary'
						onClick={() => deleteUser(profile.username)}
					>
						Удалить пользователя
					</Button>}
			</Flex>
		</Flex>
	)
}

export default ProfileHeader
