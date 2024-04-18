import { HeartOutlined } from '@ant-design/icons'
import { Descriptions, Flex, Statistic, Typography } from 'antd'
import { type FC } from 'react'
import type { MockFullUserResponse } from '../../types/MockTypes/MockFullUserResponse'

const { Paragraph } = Typography

interface ProfileDescriptionProps {
	profile: MockFullUserResponse
}

const ProfileDescription: FC<ProfileDescriptionProps> = ({ profile }) => {
	return (
		<Flex justify='space-evenly' align='center'>
			<Flex vertical style={{ maxWidth: '60%' }}>
				<Descriptions
					column={2}
					items={[
						{
							key: '1',
							label: 'Имя пользователя',
							children: 'Иван Васильевич',
						},
						{
							key: '2',
							label: 'Электронная почта',
							children: 'ivanbalyk@mail.ru',
						},
						{
							key: '3',
							label: 'Город проживания',
							children: 'Воронеж',
						},
						{
							key: '4',
							label: 'Страна проживания',
							children: 'Россия',
						},
					]}
				/>
				<Paragraph>{profile.description}</Paragraph>
			</Flex>
			<Flex vertical>
				<Statistic
					title='Количество лайков'
					value={profile.likeCount}
					prefix={<HeartOutlined />}
				/>
				<Statistic title='Количество публикаций' value={profile.postCount} />
				<Statistic
					title='Количество подписчиков'
					value={profile.subscribersCount}
				/>
			</Flex>
		</Flex>
	)
}

export default ProfileDescription
