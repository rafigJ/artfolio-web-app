import { Input, Modal, Typography, message } from 'antd'
import { FC, useState } from 'react'
import { useParams } from 'react-router-dom'
import ReportService from '../../api/ReportService'

interface ReportCommentWindowProps {
	open: boolean
	setOpen: (state: boolean) => void
	commentId: number
}

const ReportCommentWindow: FC<ReportCommentWindowProps> = ({ open, setOpen, commentId }) => {
	const [confirmLoading, setConfirmLoading] = useState(false)
	const [reportText, setReportText] = useState('')
	const params = useParams()


	const handleOk = () => {
		if (!reportText.trim()) {
			message.error('Введите текст жалобы')
			return
		}

		setConfirmLoading(true)
		ReportService.sendCommentReport(commentId,
			{ reason: reportText.trim() })
			.then(() => {
				setReportText('')
				message.success("Жалоба успешно отправлена")
				setOpen(false)
			})
			.catch(e => message.error('Ошибка отправки жалобы ' + e))
			.finally(() => {
				setConfirmLoading(false)
			})
	}

	const handleCancel = () => {
		setOpen(false)
		setReportText('')
	}

	const handleTextChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		setReportText(e.target.value)
	}

	return (
		<Modal
			title="Жалоба на комментарий"
			open={open}
			okText="Отправить"
			okType='danger'
			onOk={handleOk}
			confirmLoading={confirmLoading}
			onCancel={handleCancel}
			cancelText="Отмена"
		>
			<p style={{ marginBottom: 25 }}
			>
				<Typography.Text>
					Опишите суть жалобы:
				</Typography.Text>
				<Input.TextArea
					showCount
					rows={2}
					maxLength={50}
					onChange={handleTextChange}
					value={reportText}
				/></p>
		</Modal>
	)
}

export default ReportCommentWindow