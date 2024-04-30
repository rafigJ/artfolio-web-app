import { Input, Modal, Typography, message } from 'antd'
import { FC, useState } from 'react'

interface ReportWindowProps {
	open: boolean
	setOpen: (state: boolean) => void
}

const ReportWindow: FC<ReportWindowProps> = ({ open, setOpen }) => {
	const [confirmLoading, setConfirmLoading] = useState(false)
	const [reportText, setReportText] = useState('')


	const handleOk = () => {
		if (!reportText.trim()) {
			message.error('Введите текст жалобы')
			return
		}

		setConfirmLoading(true)
		setTimeout(() => {
			setOpen(false)
			setConfirmLoading(false)
			setReportText('')
			message.success("Жалоба успешно отправлена")
		}, 2000)
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
			title="Жалоба"
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

export default ReportWindow