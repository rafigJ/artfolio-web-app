import { Button, Flex, Form, Input, message, Typography, type UploadFile } from 'antd'
import React, { useState } from 'react'
import type { MockFullPost } from '../../types/MockTypes/MockFullPost'
import CreatePostFormPreview from '../CreatePostFormPreview/CreatePostFormPreview'
import DraggableUploadList from '../DraggableUploadList/DraggableUploadList'

const CreatePostForm = () => {
	const [post, setPost] = useState<MockFullPost>({
		'id': 33,
		'title': 'Заголовок публикации',
		'description': 'Откройте для себя увлекательный мир современного дизайна и искусства через глаза творцов и инноваторов. Эта публикация на Behance представляет вам насыщенную дозу креативности, вдохновения и творческих концепций, которые олицетворяют современные тенденции в мире дизайна и искусства. Погрузитесь в уникальные проекты, эксперименты с цветом, текстурой и формой, и вдохновляйтесь новыми идеями, которые заставят вас увидеть мир в ином свете.',
		'likeCount': 39,
		'previewMedia': 949,
		'medias': [
			12,
			13,
			14,
			15,
			29
		],
		'owner': {
			'fullName': 'Рамси Болтон',
			'username': 'boltonArts'
		}
	})
	
	const [fileList, setFileList] = useState<UploadFile[]>([])
	
	const onFinish = (values: any) => {
		if (fileList.length === 0) {
			message.error('Должна быть загружена хотя бы одна фотография')
			return
		}
		console.log(values)
		fileList.forEach(e => console.log(e.name))
	}
	
	const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setPost({ ...post, title: e.target.value })
	}
	
	const handleDescriptionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		setPost({ ...post, description: e.target.value })
	}
	
	return (
		<Flex justify='space-around' align='flex-start'>
			<div style={{ width: '50%' }}>
				<CreatePostFormPreview post={post} />
			</div>
			<div style={{ maxWidth: '50%' }}>
				<Form
					layout='vertical'
					name='post-form'
					onFinish={onFinish}
				>
					<Typography.Title level={4}>Создать публикацию</Typography.Title>
					<Form.Item name='title' label='Заголовок публикации' rules={[{ required: true }]}>
						<Input onChange={handleTitleChange} maxLength={35} />
					</Form.Item>
					<Form.Item name='description' label='Описание публикации' rules={[{ required: true }]}>
						<Input.TextArea onChange={handleDescriptionChange} showCount rows={6} maxLength={500} />
					</Form.Item>
					<DraggableUploadList fileList={fileList} setFileList={setFileList} />
					<Form.Item style={{ marginTop: '18px' }}>
						<Button type='primary' htmlType='submit'>Опубликовать</Button>
					</Form.Item>
				</Form>
			</div>
		</Flex>
	)
}

export default CreatePostForm