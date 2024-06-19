import { FileTextOutlined, HeartOutlined } from '@ant-design/icons'
import { Button, Descriptions, Flex, Statistic, Typography } from 'antd'
import pdfMake from 'pdfmake/build/pdfmake'
import pdfFonts from 'pdfmake/build/vfs_fonts'
import { type FC, useState } from 'react'
import type { FullUserResponse } from '../../types/user/FullUserResponse'
import SubscribersWindow from '../SubscribersWindow/SubscribersWindow'
import SubscriptionWindow from '../SubscribtionWindow/SubscriptionWindow'

const { Paragraph } = Typography

pdfMake.vfs = pdfFonts.pdfMake.vfs

interface ProfileDescriptionProps {
	profile: FullUserResponse
}

const ProfileDescription: FC<ProfileDescriptionProps> = ({ profile }) => {
	const [openSubscribers, setOpenSubscribers] = useState(false)
	const showSubscribers = () => {
		setOpenSubscribers(true)
	}
	const [openSubscriptions, setOpenSubscribtions] = useState(false)
	const showSubscriptions = () => {
		setOpenSubscribtions(true)
	}
	
	const handleExportDescription = () => {
		window.ym(97163910, 'reachGoal', 'exportTo')
		const docDefinition = {
			content: [
				{ text: 'Имя пользователя: ', bold: true },
				profile?.fullName,
				{ text: 'Электронная почта: ', bold: true },
				profile?.email,
				{ text: 'Город проживания: ', bold: true },
				profile?.city,
				{ text: 'Страна проживания: ', bold: true },
				profile?.country,
				{ text: 'Описание:', bold: true },
				profile?.description,
				{ text: 'Количество лайков: ', bold: true },
				profile?.likeCount,
				{ text: 'Количество публикаций: ', bold: true },
				profile?.postCount,
				{ text: 'Количество подписчиков: ', bold: true },
				profile?.subscribersCount
			]
		}
		
		pdfMake.createPdf(docDefinition).download('profile_description.pdf')
	}
	
	return (
		<>
			<SubscribersWindow open={openSubscribers} setOpen={setOpenSubscribers} user={profile} />
			<SubscriptionWindow open={openSubscriptions} setOpen={setOpenSubscribtions} user={profile} />
			<Flex justify='space-evenly' align='center'>
				<Flex vertical style={{ maxWidth: '60%' }}>
					<Descriptions
						column={2}
						items={[
							{
								key: '1',
								label: 'Имя пользователя',
								children: profile.username
							},
							{
								key: '2',
								label: 'Электронная почта',
								children: profile.email
							},
							{
								key: '3',
								label: 'Город проживания',
								children: profile.city
							},
							{
								key: '4',
								label: 'Страна проживания',
								children: profile.country
							}
						]} />
					<Paragraph>{profile?.description}</Paragraph>
				</Flex>
				<Flex vertical>
					<Button
						icon={<FileTextOutlined />}
						style={{ marginBottom: 20 }}
						onClick={handleExportDescription}>
						Экспортировать описание
					</Button>
					<Statistic
						title='Количество лайков'
						value={profile?.likeCount}
						prefix={<HeartOutlined />} />
					<Statistic title='Количество публикаций' value={profile?.postCount} />
					<div onClick={showSubscribers} style={{ cursor: 'pointer' }}>
						<Statistic
							title='Подписчики:'
							value={profile?.subscribersCount} />
					</div>
					<div onClick={showSubscriptions} style={{ cursor: 'pointer' }}>
						<Statistic
							title='Подписки'
							value={profile?.followingCount} />
					</div>
				</Flex>
			</Flex>
		</>
	)
}

export default ProfileDescription
