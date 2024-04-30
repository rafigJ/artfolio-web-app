import { Checkbox, Table, TableProps } from 'antd'
import { FC } from 'react'

interface ReportTableProps {
	getChecked: boolean
}

const ReportTable: FC<ReportTableProps> = ({ getChecked }) => {
	interface DataType {
		key: string
		senderProfile: string
		post: string
		commentText: string
		reportText: string
		authorProfile: string
		date: string
		checked: boolean
	}
	console.log(getChecked)

	const columns: TableProps<DataType>['columns'] = [
		{
			title: 'Отправитель жалобы',
			dataIndex: 'senderProfile',
			key: 'senderProfile',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Публикация',
			dataIndex: 'post',
			key: 'post',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Текст комментария',
			dataIndex: 'commentText',
			key: 'commentText',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Текст жалобы',
			dataIndex: 'reportText',
			key: 'reportText',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Профиль автора поста/комментария',
			dataIndex: 'authorProfile',
			key: 'authorProfile',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Время оставления жалобы',
			dataIndex: 'date',
			key: 'date',
			render: (text) => <span>{text}</span>,
		},
		{
			title: 'Статус',
			dataIndex: 'checked',
			key: 'checked',
			render: (checked) => <span><Checkbox defaultChecked={checked}>Рассмотрена</Checkbox></span>,
		},
	]

	const data: DataType[] = [
		{
			key: '1',
			senderProfile: 'user123',
			post: 'Нью-Йорк, Парк № 1',
			commentText: 'Какой-то текст комментария здесь',
			reportText: 'Какой-то текст жалобы здесь',
			authorProfile: 'commentator456',
			date: '2022-03-30 09:00',
			checked: true,
		},
		{
			key: '2',
			senderProfile: 'admin007',
			post: 'Лондон, Парк № 1',
			commentText: 'Еще один текст комментария',
			reportText: 'Еще один текст жалобы',
			authorProfile: 'user789',
			date: '2022-03-31 12:30',
			checked: false,
		},
		{
			key: '3',
			senderProfile: 'testuser',
			post: 'Сидней, Парк № 1',
			commentText: 'Еще один комментарий',
			reportText: 'Еще одна жалоба',
			authorProfile: 'testcommentator',
			date: '2022-04-01 15:45',
			checked: true,
		},
		{
			key: '4',
			senderProfile: 'webmaster',
			post: 'Париж, Парк № 1',
			commentText: 'И еще один текст комментария',
			reportText: 'И еще один текст жалобы',
			authorProfile: 'commentor',
			date: '2022-04-02 10:20',
			checked: false,
		},
		{
			key: '5',
			senderProfile: 'guest',
			post: 'Берлин, Парк № 1',
			commentText: 'Финальный комментарий',
			reportText: 'Финальная жалоба',
			authorProfile: 'anon',
			date: '2022-04-03 18:00',
			checked: true,
		},
	]


	const filteredData = data.filter(item => item.checked === getChecked)

	return (
		<Table columns={columns} dataSource={filteredData} />
	)
}

export default ReportTable