import { Comment } from '@ant-design/compatible'
import { Avatar, Button, Form, Input, message } from 'antd'
import React, { useContext, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { API_URL } from '../../api'
import CommentService from '../../api/CommentService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import { CommentResponse } from '../../types/comment/CommentResponse'
import CommentList from '../CommentList/CommentList'

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
				onPressEnter={(event) => {
					if (event.key === 'Enter' && !event.shiftKey) {
						event.preventDefault()
						onSubmit()
					}
				}}
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
	const [submitting, setSubmitting] = useState(false)
	const [value, setValue] = useState('')
	
	const navigate = useNavigate()
	const params = useParams()
	const { isAuth, authCredential } = useContext(AuthContext)
	
	const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		if (!isAuth) {
			navigate('/login')
		}
		setValue(e.target.value)
	}
	
	const [comments, setComments] = useState<CommentResponse[]>([])
	
	const [fetchComments, isLoading, isError] = useFetching(async (id) => {
		const response = await CommentService.getComments(id, 0, 1000)
		setComments(p => [...p, ...response.data.content])
	})
	
	const handleSubmit = () => {
		if (!isAuth) {
			navigate('/login')
		}
		
		if (!value) return
		
		setSubmitting(true)
		CommentService.createComment(Number(params.id), { comment: value.trim() })
			.then(c => setComments(p => [...p, c.data]))
			.catch(e => message.error('Ошибка комментирования ' + e))
			.finally(() => {
				setSubmitting(false)
				setValue('')
			})
	}
	
	useEffect(() => {
		fetchComments(params.id)
	}, [params.id])
	
	if (isLoading || isError) {
		return <></>
	}
	
	return (
		<>
			{comments.length > 0 ? (
				<CommentList data={comments} setData={setComments} />
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
