import { Modal, message } from 'antd'
import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import UserService from '../../api/UserService'
import { useFetching } from '../../hooks/useFetching'

interface ConfirmProps {
	open: boolean
	setOpen: (state: boolean) => void
	username: string
}

const ConfirmWindow: FC<ConfirmProps> = ({ open, setOpen, username }) => {
	const navigate = useNavigate()

	const [deleteUser] = useFetching(async (username: string) => {
		await UserService.deleteUser(username)
			.then(() => {
				navigate('/login')
				message.success("Пользователь успешно удалён")
			})
			.catch(e => {
				message.error("Ошибка удаления пользователя" + e)
			})
	})

	const handleCancel = () => {
		setOpen(false)
	}

	return (
		<Modal
			title="Вы уверены, что хотите удалить пользователя?"
			open={open}
			okText="Удалить"
			okType='danger'
			onOk={() => deleteUser(username)}
			onCancel={handleCancel}
			cancelText="Отмена"
		>
		</Modal>
	)
}

export default ConfirmWindow