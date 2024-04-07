import { Button, Flex, Form, Input, message, Typography, type UploadFile } from 'antd'
import React, { useState } from 'react'
import type { Product } from '../../types/MockTypes/Product'
import CreatePostFormPreview from '../CreatePostFormPreview/CreatePostFormPreview'
import DraggableUploadList from '../DraggableUploadList/DraggableUploadList'

const CreatePostForm = () => {
	const [post, setPost] = useState<Product>({
		'albumId': 1,
		'id': 41,
		'title': 'Заголовок поста',
		'url': 'https://upload.wikimedia.org/wikipedia/commons/3/36/Ryan_Gosling_%2835397111013%29.jpg',
		'thumbnailUrl': 'https://upload.wikimedia.org/wikipedia/commons/3/36/Ryan_Gosling_%2835397111013%29.jpg'
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
						<Input.TextArea showCount rows={6} maxLength={300} />
					</Form.Item>
					<DraggableUploadList fileList={fileList} setFileList={setFileList}/>
					<Form.Item style={{ marginTop: '18px' }}>
						<Button type='primary' htmlType='submit'>Опубликовать</Button>
					</Form.Item>
				</Form>
			</div>
		</Flex>
	)
}

export default CreatePostForm