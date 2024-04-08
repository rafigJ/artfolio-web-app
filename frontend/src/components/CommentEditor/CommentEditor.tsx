import { Comment } from '@ant-design/compatible'
import { Avatar, Button, Form, Input } from 'antd'
import React, { useState } from 'react'
import CommentList, { type CommentItem } from '../CommentList/CommentList'

const { TextArea } = Input

interface EditorProps {
	onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
	onSubmit: () => void;
	submitting: boolean;
	value: string;
}

// todo добавить в реализацию переход по ссылке, в случае если пользователь не вошел в профиль
const Editor = ({ onChange, onSubmit, submitting, value }: EditorProps) => (
	<>
		<Form.Item>
			<TextArea placeholder='Введите комментарий' maxLength={300} rows={4} onChange={onChange} value={value} />
		</Form.Item>
		<Form.Item>
			<Button htmlType='submit' loading={submitting} onClick={onSubmit} type='primary'>
				Отправить
			</Button>
		</Form.Item>
	</>
)

const CommentEditor: React.FC = () => {
	const [comments, setComments] = useState<CommentItem[]>([])
	const [submitting, setSubmitting] = useState(false)
	const [value, setValue] = useState('')
	
	const handleSubmit = () => {
		if (!value) return
		
		setSubmitting(true)
		
		setTimeout(() => {
			setSubmitting(false)
			setValue('')
			setComments([
				...comments,
				{
					author: 'Han Solo',
					avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=3',
					content: <p>{value}</p>,
					datetime: '2016-11-22'
				}
			])
		}, 1000)
	}
	
	const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		setValue(e.target.value)
	}
	
	return (
		<>
			{comments.length > 0 ? <CommentList data={comments} /> : <div style={{marginTop: 28}}/>}
			<Comment
				style={{ backgroundColor: 'transparent' }}
				avatar={<Avatar src='https://api.dicebear.com/7.x/miniavs/svg?seed=3' alt='Han Solo' />}
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