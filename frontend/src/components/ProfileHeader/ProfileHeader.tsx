import { AntDesignOutlined } from '@ant-design/icons'
import { Avatar, Button, Flex, Typography } from 'antd'
import { type FC, useState } from 'react'
import { API_URL } from '../../api'
import type { FullUserResponse } from '../../types/FullUserResponse'

interface ProfileHeaderProps {
	profile: FullUserResponse
}

const ProfileHeader: FC<ProfileHeaderProps> = ({ profile }) => {
	const [userIsSubscribed, setIsSubscribed] = useState(false)
	
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
					onClick={() => setIsSubscribed(!userIsSubscribed)}
				>
					{userIsSubscribed ? 'Отписаться' : 'Подписаться'}
				</Button>
				<Button href={`mailto://${profile?.email}`}>Связаться</Button>
			</Flex>
		</Flex>
	)
}

export default ProfileHeader
