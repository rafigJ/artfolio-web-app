import { FileTextOutlined, HeartOutlined } from '@ant-design/icons'
import { Button, Descriptions, Flex, Statistic, Typography } from 'antd'
import pdfMake from 'pdfmake/build/pdfmake'
import pdfFonts from 'pdfmake/build/vfs_fonts'
import { type FC } from 'react'
import type { FullUserResponse } from '../../types/FullUserResponse'

const { Paragraph } = Typography

pdfMake.vfs = pdfFonts.pdfMake.vfs

interface ProfileDescriptionProps {
	profile: FullUserResponse
}

const ProfileDescription: FC<ProfileDescriptionProps> = ({ profile }) => {
	const handleExportDescription = () => {
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
				100,
				{ text: 'Количество публикаций: ', bold: true },
				5,
				{ text: 'Количество подписчиков: ', bold: true },
				100,
			]
		}

		pdfMake.createPdf(docDefinition).download('profile_description.pdf')
	}

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
					value={3}
					prefix={<HeartOutlined />}
				/>
				<Statistic title='Количество публикаций' value={3} />
				<Statistic
					title='Количество подписчиков'
					value={100}
				/>
			</Flex>
		</Flex >
	)
}

export default ProfileDescription
