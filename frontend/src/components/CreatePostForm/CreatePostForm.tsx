import {
	Affix,
	Button,
	Flex,
	Form,
	Input,
	message,
	Typography,
	type UploadFile,
} from 'antd'
import React, { useState } from 'react'
import type { MockPostRequest } from '../../types/MockTypes/MockPostRequest'
import CreatePostFormPreview from '../CreatePostFormPreview/CreatePostFormPreview'
import DraggableUploadList from '../DraggableUploadList/DraggableUploadList'

/**
 * Основной компонент CreatePostPage.
 * Используется для создания поста (публикации).
 * В форме используется валидация заголовка и описания.
 * Также мы проверяем, что список файлов не пуст.
 * TODO: отправление моковых запросов, превью PostCard.
 * TODO: добавить элемент формы загрузка Preview (обложки публикации).
 */
const CreatePostForm = () => {
	const [post, setPost] = useState<MockPostRequest>({
		title: 'Заголовок публикации',
		description:
			'Откройте для себя увлекательный мир современного дизайна ' +
			'и искусства через глаза творцов и инноваторов. Эта публикация на Artfolio ' +
			'представляет вам насыщенную дозу креативности, вдохновения и творческих концепций, ' +
			'которые олицетворяют современные тенденции в мире дизайна и искусства. ' +
			'Погрузитесь в уникальные проекты, эксперименты с цветом, текстурой и формой, ' +
			'и вдохновляйтесь новыми идеями, которые заставят вас увидеть мир в ином свете.',
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

	const handleDescriptionChange = (
		e: React.ChangeEvent<HTMLTextAreaElement>
	) => {
		setPost({ ...post, description: e.target.value })
	}

	return (
		<Flex justify='space-around' align='flex-start'>
			<div style={{ width: '50%' }}>
				<CreatePostFormPreview post={post} fileList={fileList} />
			</div>
			<Affix style={{ maxWidth: '45%' }}>
				<div>
					<Form layout='vertical' name='post-form' onFinish={onFinish}>
						<Typography.Title level={4}>Создать публикацию</Typography.Title>
						<Form.Item
							name='title'
							label='Заголовок публикации'
							rules={[{ required: true }]}
						>
							<Input onChange={handleTitleChange} maxLength={35} />
						</Form.Item>
						<Form.Item
							name='description'
							label='Описание публикации'
							rules={[{ required: true }]}
						>
							<Input.TextArea
								onChange={handleDescriptionChange}
								showCount
								rows={6}
								maxLength={500}
							/>
						</Form.Item>
						<DraggableUploadList
							fileList={fileList}
							setFileList={setFileList}
						/>
						<Form.Item style={{ marginTop: '18px' }}>
							<Button type='primary' htmlType='submit'>
								Опубликовать
							</Button>
						</Form.Item>
					</Form>
				</div>
			</Affix>
		</Flex>
	)
}

export default CreatePostForm
