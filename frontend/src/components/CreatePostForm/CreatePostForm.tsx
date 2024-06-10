import { Affix, Button, Flex, Form, Input, message, Typography, type UploadFile } from 'antd'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import PostService from '../../api/PostService'
import { useFetching } from '../../hooks/useFetching'
import type { MockPostRequest } from '../../types/MockTypes/MockPostRequest'
import type { PostRequest } from '../../types/PostRequest'
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
	const navigate = useNavigate()
	const [post, setPost] = useState<PostRequest>({
		name: 'Заголовок публикации',
		description:
			'Откройте для себя увлекательный мир современного дизайна ' +
			'и искусства через глаза творцов и инноваторов. Эта публикация на Artfolio ' +
			'представляет вам насыщенную дозу креативности, вдохновения и творческих концепций, ' +
			'которые олицетворяют современные тенденции в мире дизайна и искусства. ' +
			'Погрузитесь в уникальные проекты, эксперименты с цветом, текстурой и формой, ' +
			'и вдохновляйтесь новыми идеями, которые заставят вас увидеть мир в ином свете.'
	})

	const [fileList, setFileList] = useState<UploadFile[]>([])

	const [createPost, isLoading] = useFetching(async (post: PostRequest, files: File[]) => {
		await PostService.createPost(post, files)
			.then(r => {
				navigate(`/posts/${r.data.id}`)
				message.success(`Вы успешно создали пост ${r.data.name}`)
			}).catch(e => {
				message.error(`Ошибка создания публикации ${e}`)
			})
	})


	const onFinish = (values: PostRequest) => {
		const trimmedValues = {
			name: values.name.trim(),
			description: values.description.trim()
		}
		if (fileList.length === 0) {
			message.error('Должна быть загружена хотя бы одна фотография')
			return
		}
		window.ym(97163910, 'reachGoal', 'createSuccess')
		console.log(fileList)
		createPost(trimmedValues, fileList.map(e => e?.originFileObj as File))
	}

	const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setPost({ ...post, name: e.target.value })
	}

	const handleDescriptionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
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
							name='name'
							label='Заголовок публикации'
							rules={[
								{ required: true, message: 'Введите название публикации!' },
								{ max: 35, message: 'Название должно содержать не более 35 символов' },
								{
									validator: (_, value) => {
										if (value.trim().replace(/\s/g, '').length < 3) {
											return Promise.reject('Заголовок должен быть не меньше 3 символов')
										}
										return Promise.resolve()
									}
								}
							]}
						>
							<Input onChange={handleTitleChange} maxLength={36} />
						</Form.Item>
						<Form.Item
							name='description'
							label='Описание публикации'
							rules={[{ required: true, message: 'Введите описание публикации!' },
							{ max: 400, message: 'Описание публикации должно содержать не более 400 символов' }
							]}
						>
							<Input.TextArea
								onChange={handleDescriptionChange}
								showCount
								rows={3}
								maxLength={400}
								styles={{ textarea: { maxHeight: '5rem' } }}
							/>
						</Form.Item>
						<DraggableUploadList
							fileList={fileList}
							setFileList={setFileList}
						/>
						<Form.Item style={{ marginTop: '18px' }}>
							<Button loading={isLoading} type='primary' htmlType='submit'>
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
