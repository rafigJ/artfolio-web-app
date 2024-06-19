import { DownOutlined } from '@ant-design/icons'
import { Button, Dropdown, MenuProps, Space, Table, TableProps, message } from 'antd'
import { format } from 'date-fns'
import { FC, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import ReportService from '../../api/ReportService'
import { useFetching } from '../../hooks/useFetching'
import { ReportType, Reviewed } from '../../types/reports/ReportRequest'
import { ReportResponse } from '../../types/reports/ReportResponse'



const ReportTableComments: FC = () => {

	const [listReviewed, setListReviewed] = useState(false)

	const items: MenuProps['items'] = [
		{
			key: '1',
			label: 'Нерассмотренные',
			onClick: () => setListReviewed(false),
		},
		{
			key: '2',
			label: 'Рассмотренные',
			onClick: () => setListReviewed(true),
		},
	]

	const columns: TableProps<ReportResponse>['columns'] = [
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
			title: 'Профиль автора комментария',
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
			dataIndex: 'createTime',
			key: 'createTime',
			render: (createTime) =>
				<span>
					{format(new Date(createTime), 'dd.MM.yyyy HH:mm:ss')}
				</span>
		},
		{
			title: 'Статус',
			dataIndex: 'reviewed',
			key: 'reviewed',
			render: (reviewed: boolean, record: ReportResponse) =>
				<span>
					{reviewed ? (
						<Button
							type='link'
							onClick={() => handleReview(record.id, record.reviewed)}
						>
							Отметить как нерассмотренную
						</Button>
					) : (
						<Button
							type='link'
							onClick={() => handleReview(record.id, record.reviewed)}
						>
							Отметить как рассмотренную
						</Button>
					)}
				</span>,
		},
	]

	const [reports, setReports] = useState<ReportResponse[]>([])

	const [fetchReports, isLoading, isError] = useFetching(async () => {
		const response = await ReportService.getReports(ReportType.comment, 0, 1000, (listReviewed ? Reviewed.TRUE : Reviewed.FALSE))
		setReports(response.data.content)
	})

	useEffect(() => {
		fetchReports()
	}, [listReviewed])

	if (isLoading || isError) {
		return <></>
	}

	const handleReview = async (reportId: number, reportReviewed: boolean) => {
		ReportService.reviewCommentReport(reportId, !reportReviewed)
			.then(() => {
				fetchReports()
				listReviewed ?
					message.success("Жалоба отмечена как нерассмотренная")
					:
					message.success("Жалоба отмечена как рассмотренная")
			})
			.catch(e => message.error('Ошибка отметки жалобы ' + e))
	}

	return (
		<><Dropdown menu={{ items }} placement='bottomLeft'>
			<Space style={{ marginBottom: 20 }}>
				{listReviewed ? 'Рассмотренные' : 'Нерассмотренные'}
				<DownOutlined />
			</Space>
		</Dropdown>
			<Table columns={columns} dataSource={reports} /></>
	)
}

export default ReportTableComments