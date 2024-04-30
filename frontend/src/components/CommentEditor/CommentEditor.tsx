import { Comment } from '@ant-design/compatible'
import { Avatar, Button, Form, Input } from 'antd'
import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import { AuthContext } from '../../context'
import CommentList, { type CommentItem } from '../CommentList/CommentList'

const { TextArea } = Input

interface EditorProps {
	onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void
	onSubmit: () => void
	submitting: boolean
	value: string
}

// todo добавить в реализацию переход по ссылке, в случае если пользователь не вошел в профиль
const Editor = ({ onChange, onSubmit, submitting, value }: EditorProps) => (
	<>
		<Form.Item>
			<TextArea
				placeholder='Введите комментарий'
				maxLength={300}
				rows={4}
				onChange={onChange}
				value={value}
			/>
		</Form.Item>
		<Form.Item>
			<Button
				htmlType='submit'
				loading={submitting}
				onClick={onSubmit}
				type='primary'
			>
				Отправить
			</Button>
		</Form.Item>
	</>
)

const CommentEditor: React.FC = () => {
	const [comments, setComments] = useState<CommentItem[]>([])
	const [submitting, setSubmitting] = useState(false)
	const [value, setValue] = useState('')
	const navigate = useNavigate()
	const { isAuth, authCredential } = useContext(AuthContext)
	
	const handleSubmit = () => {
		if (!isAuth) {
			navigate('/login')
		}
		
		if (!value) return
		
		setSubmitting(true)
		
		setTimeout(() => {
			setSubmitting(false)
			setValue('')
			setComments(prevComments => [
				...prevComments,
				{
					author: authCredential?.name,
					avatar: `${API_URL}/user/${authCredential?.username}/avatar`,
					content: <p>{value}</p>,
					datetime: new Date().toISOString(),
					id: prevComments.length
				}
			])
		}, 1000)
	}
	
	const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		if (!isAuth) {
			navigate('/login')
		}
		
		setValue(e.target.value)
	}
	
	return (
		<>
			{comments.length > 0 ? (
				<CommentList data={comments} />
			) : (
				<div style={{ marginTop: 28 }} />
			)}
			<Comment
				style={{ backgroundColor: 'transparent' }}
				avatar={isAuth ?
					<Avatar
						src={`${API_URL}/user/${authCredential?.username}/avatar`}
						alt={authCredential?.name}
						onClick={() => navigate('/profile/' + authCredential?.username)}
					/>
					:
					<Avatar
						src='https://api.dicebear.com/7.x/miniavs/svg?seed=3'
						alt='Han Solo'
					/>
				}
				content={
					<Editor
						onChange={handleChange}
						onSubmit={handleSubmit}
						submitting={submitting}
						value={value}
					/>
				}
			/>
		</>
	)
}

export default CommentEditor
