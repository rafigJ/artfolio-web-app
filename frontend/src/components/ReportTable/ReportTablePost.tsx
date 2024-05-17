import { DownOutlined } from '@ant-design/icons'
import { Checkbox, Dropdown, MenuProps, Space, Table, TableProps } from 'antd'
import { FC, useState } from 'react'
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
			id: 1,
			postId: 20,
			reason: "Какой-то текст жалобы здесь",
			reviewed: true,
			targetUser: {
				fullName: "Джон Сноу",
				username: 'commentator456',
			},
			sendler: {
				fullName: "Джон Сноу",
				username: 'user123',
			}
		},
		{
			id: 2,
			postId: 33,
			reason: "Очень много крови!",
			reviewed: false,
			targetUser: {
				fullName: "Рамси Болтон",
				username: 'boltonArts'
			},
			sendler: {
				fullName: "Рамси Болтон",
				username: 'boltonArts'
			}
		},
		{
			id: 3,
			postId: 45,
			reason: "Слишком много рекламы",
			reviewed: true,
			targetUser: {
				fullName: "Джон Сноу",
				username: 'johnsnow22'
			},
			sendler: {
				fullName: "Санса Старк",
				username: 'sansa_stark'
			}
		},
		{
			id: 4,
			postId: 54,
			reason: "Нецензурные выражения",
			reviewed: false,
			targetUser: {
				fullName: "Арья Старк",
				username: 'aryastark11'
			},
			sendler: {
				fullName: "Тирион Ланнистер",
				username: 'the_imp'
			}
		},
		{
			id: 5,
			postId: 67,
			reason: "Нарушение правил сообщества",
			reviewed: true,
			targetUser: {
				fullName: "Тирион Ланнистер",
				username: 'the_imp'
			},
			sendler: {
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