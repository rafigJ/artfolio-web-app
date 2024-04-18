import { AntDesignOutlined } from '@ant-design/icons'
import { Avatar, Button, Flex, Typography } from 'antd'
import { useState } from 'react'

const ProfileHeader = () => {
	const [userIsSubscribed, setIsSubscribed] = useState(false)

	return (
		<Flex
			style={{
				background: 'white',
				minHeight: 280,
				padding: 24,
				borderRadius: 8,
			}}
			justify={'center'}
		>
			<Avatar
				src='https://api.dicebear.com/7.x/miniavs/svg?seed=3'
				size={150}
				icon={<AntDesignOutlined />}
			/>
			<Flex vertical style={{ marginLeft: '15px' }}>
				<Typography.Title level={3}>Иван Васильевич</Typography.Title>
				<Typography.Text>Воронеж, Россия</Typography.Text>
				<Button
					style={{ margin: '10px 0' }}
					danger={userIsSubscribed}
					type='primary'
					onClick={() => setIsSubscribed(!userIsSubscribed)}
				>
					{userIsSubscribed ? 'Отписаться' : 'Подписаться'}
				</Button>
				<Button href='mailto://rafigdzabbarov0410@gmail.com'>Связаться</Button>
			</Flex>
		</Flex>
	)
}

export default ProfileHeader
