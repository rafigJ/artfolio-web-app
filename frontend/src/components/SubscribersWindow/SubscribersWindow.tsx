import { Button, Modal, Table } from 'antd'
import { FC, useState } from 'react'
import { UserResponce } from '../../types/UserResponce'

interface SubscribersWindowProps {
	user: string
	open: boolean
	setOpen: (state: boolean) => void
}

const SubscribersWindow: FC<SubscribersWindowProps> = ({ user, open, setOpen }) => {
	const [userIsSubscribed, setIsSubscribed] = useState(true)
	const columns = [
		{
			title: 'Полное имя',
			dataIndex: 'fullName',
			key: 'fullName',
		},
		{
			title: 'Имя пользователя',
			dataIndex: 'username',
			key: 'username',
		},
		{
			title: '',
			dataIndex: 'user',
			key: 'userIsSubscribed',
			render: (text: string) =>
				<span>
					<Button
						style={{ margin: '10px 0' }}
						danger={userIsSubscribed}
						type='primary'
						onClick={() => setIsSubscribed(!userIsSubscribed)}
					>
						{userIsSubscribed ? 'Отписаться' : 'Подписаться'}
					</Button>
				</span>,
		},
	]

	const handleCancel = () => {
		setOpen(false)
	}

	const subscribers: UserResponce[] = [
		{
			fullName: "Джон Сноу",
			username: 'commentator456'
		},
		{
			fullName: "Рамси Болтон",
			username: 'boltonArts'
		}
	]

	return (
		<Modal
			title="Список подписчиков"
			open={open}
			onCancel={handleCancel}
			footer={null}
		>
			<Table columns={columns} dataSource={subscribers} />
		</Modal>
	)
}

export default SubscribersWindow