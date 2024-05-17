import { DownOutlined } from '@ant-design/icons'
import { Checkbox, Dropdown, MenuProps, Space, Table, TableProps } from 'antd'
import { FC, useState } from 'react'
import { ReportResponce } from '../../types/ReportResponce'



const ReportTableComments: FC = () => {

	const [reviewed, setReviewed] = useState(false)

	const items: MenuProps['items'] = [
		{
			key: '1',
			label: 'Нерассмотренные',
			onClick: () => setReviewed(false),
		},
		{
			key: '2',
			label: 'Рассмотренные',
			onClick: () => setReviewed(true),
		},
	]

	const columns: TableProps<ReportResponce>['columns'] = [
		{
			title: 'ID',
			dataIndex: 'id',
			key: 'id',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Отправитель жалобы',
			dataIndex: ['sendler', 'username'],
			key: 'senderProfile',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Публикация',
			dataIndex: 'postId',
			key: 'post',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Комментарий',
			dataIndex: 'comment',
			key: 'comment',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Текст жалобы',
			dataIndex: 'reason',
			key: 'reason',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Профиль автора поста',
			dataIndex: ['targetUser', 'username'],
			key: 'authorProfile',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Статус',
			dataIndex: 'reviewed',
			key: 'reviewed',
			render: (reviewed) => <span><Checkbox defaultChecked={reviewed}>Рассмотрена</Checkbox></span>,
		},
	]

	const data: ReportResponce[] = [
		{
			id: 159,
			postId: 33,
			commentId: 52,
			comment: "Б**%%ТЬ, КАКАЯ ЖЕ Х***А!",
			reason: "Очень много мата!",
			reviewed: false,
			targetUser: {
				fullName: "Рамси Болтон",
				username: "boltonArts"
			},
			sendler: {
				fullName: "Рамси Болтон",
				username: "boltonArts"
			}
		},
		{
			id: 160,
			postId: 34,
			commentId: 53,
			comment: "Ваша мать в ***де!",
			reason: "Оскорбительный комментарий",
			reviewed: false,
			targetUser: {
				fullName: "Тирион Ланнистер",
				username: "tyrionFan"
			},
			sendler: {
				fullName: "Тирион Ланнистер",
				username: "tyrionFan"
			}
		},
		{
			id: 161,
			postId: 35,
			commentId: 54,
			comment: "Какая прекрасная погода сегодня!",
			reason: "Нарушение правил сообщества",
			reviewed: true,
			targetUser: {
				fullName: "Джон Сноу",
				username: "johnSnow"
			},
			sendler: {
				fullName: "Джон Сноу",
				username: "johnSnow"
			}
		},
		{
			id: 162,
			postId: 36,
			commentId: 55,
			comment: "Хороший пост, спасибо за информацию!",
			reason: "Нецензурная лексика",
			reviewed: true,
			targetUser: {
				fullName: "Дейенерис Таргариен",
				username: "dragonQueen"
			},
			sendler: {
				fullName: "Дейенерис Таргариен",
				username: "dragonQueen"
			}
		},
		{
			id: 163,
			postId: 37,
			commentId: 56,
			comment: "Очень интересный материал!",
			reason: "Нарушение правил сообщества",
			reviewed: false,
			targetUser: {
				fullName: "Санса Старк",
				username: "sansaFan"
			},
			sendler: {
				fullName: "Санса Старк",
				username: "sansaFan"
			}
		}
	]


	const filteredData = data.filter(item => item.reviewed === reviewed)

	return (
		<><Dropdown menu={{ items }} placement='bottomLeft'>
			<Space style={{ marginBottom: 20 }}>
				{reviewed ? 'Рассмотренные' : 'Нерассмотренные'}
				<DownOutlined />
			</Space>
		</Dropdown>
			<Table columns={columns} dataSource={filteredData} /></>
	)
}

export default ReportTableComments