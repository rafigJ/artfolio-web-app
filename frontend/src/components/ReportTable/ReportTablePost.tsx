import { DownOutlined } from '@ant-design/icons'
import { Button, Dropdown, MenuProps, Space, Table, TableProps } from 'antd'
import { FC, useState } from 'react'
import { Link } from 'react-router-dom'
import { ReportResponce } from '../../types/ReportResponce'


const ReportTablePost: FC = () => {

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
			dataIndex: ['sender', 'username'],
			key: 'senderProfile',
			render: (sender) =>
				<span>
					<Link to={`/profile/${sender}`}>
						{sender}
					</Link>
				</span>,
		},
		{
			title: 'Публикация',
			dataIndex: 'postId',
			key: 'post',
			render: (post) =>
				<span>
					<Link to={`/posts/${post}`}>
						{post}
					</Link>
				</span>,
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
			render: (author) =>
				<span>
					<Link to={`/profile/${author}`}>
						{author}
					</Link>
				</span>,
		},
		{
			title: 'Время оставления жалобы',
			dataIndex: 'time',
			key: 'time',
		},
		{
			title: 'Статус',
			dataIndex: 'reviewed',
			key: 'reviewed',
			render: (reviewed) =>
				<span>
					{reviewed ? (
						<Button type="link">
							Отметить как нерассмотренную
						</Button>
					) : (
						<Button type="link">
							Отметить как рассмотренную
						</Button>
					)}
				</span>,
		},
	]

	const data: ReportResponce[] = [
		{
			id: 1,
			postId: 20,
			reason: "Какой-то текст жалобы здесь",
			reviewed: true,
			time: '2022-03-30 12:45',
			targetUser: {
				fullName: "Джон Сноу",
				username: 'commentator456',
			},
			sender: {
				fullName: "Джон Сноу",
				username: 'user123',
			}
		},
		{
			id: 2,
			postId: 33,
			reason: "Очень много крови!",
			reviewed: false,
			time: '2022-03-30 12:45',
			targetUser: {
				fullName: "Рамси Болтон",
				username: 'boltonArts'
			},
			sender: {
				fullName: "Рамси Болтон",
				username: 'boltonArts'
			}
		},
		{
			id: 3,
			postId: 45,
			reason: "Слишком много рекламы",
			reviewed: true,
			time: '2022-03-30 12:45',
			targetUser: {
				fullName: "Джон Сноу",
				username: 'johnsnow22'
			},
			sender: {
				fullName: "Санса Старк",
				username: 'sansa_stark'
			}
		},
		{
			id: 4,
			postId: 54,
			reason: "Нецензурные выражения",
			reviewed: false,
			time: '2022-03-30 12:45',
			targetUser: {
				fullName: "Арья Старк",
				username: 'aryastark11'
			},
			sender: {
				fullName: "Тирион Ланнистер",
				username: 'the_imp'
			}
		},
		{
			id: 5,
			postId: 67,
			reason: "Нарушение правил сообщества",
			reviewed: true,
			time: '2022-03-30 12:45',
			targetUser: {
				fullName: "Тирион Ланнистер",
				username: 'the_imp'
			},
			sender: {
				fullName: "Серсея Ланнистер",
				username: 'cersei_queen'
			}
		},
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

export default ReportTablePost