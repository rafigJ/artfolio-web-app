import { Affix, Button, Flex, Form, Input, message, Spin, Typography, type UploadFile } from 'antd'
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { API_URL } from '../../api'
import PostService from '../../api/PostService'
import { useFetching } from '../../hooks/useFetching'
import type { PostRequest } from '../../types/PostRequest'
import CreatePostFormPreview from '../CreatePostFormPreview/CreatePostFormPreview'
import DraggableUploadList from '../DraggableUploadList/DraggableUploadList'

const getCroppedImage = async (imageDataUrl: string): Promise<string> => {
	return new Promise<string>((resolve, reject) => {
		const canvas = document.createElement('canvas')
		const ctx = canvas.getContext('2d')
		if (!ctx) {
			reject(new Error('Unable to get canvas context'))
			return
		}
		
		const img = new Image()
		img.src = imageDataUrl
		img.onload = () => {
			const aspectRatio = img.width / img.height
			let width = 200
			let height = 200
			
			// Determine the dimensions for resizing the image
			if (aspectRatio > 1) {
				height = width / aspectRatio
			} else {
				width = height * aspectRatio
			}
			
			// Set canvas size
			canvas.width = 200
			canvas.height = 200
			
			// Calculate offset to center the image
			const offsetX = (200 - width) / 2
			const offsetY = (200 - height) / 2
			
			// Draw the image onto the canvas
			ctx.clearRect(0, 0, 200, 200)
			ctx.drawImage(img, offsetX, offsetY, width, height)
			
			// Convert canvas to PNG data URL
			const croppedImageDataUrl = canvas.toDataURL('image/png')
			
			resolve(croppedImageDataUrl) // Resolve with the cropped image data URL
		}
		img.onerror = error => reject(error)
	})
}

const EditPostForm: React.FC = () => {
	const navigate = useNavigate()
	const params = useParams()
	const [post, setPost] = useState<PostRequest>({} as PostRequest)
	const [fileList, setFileList] = useState<UploadFile[]>([])
	const [fileBlobMap, setMap] = useState<Map<string, Blob>>(new Map())
	
	const updateMap = (key: string, value: Blob) => {
		setMap(prevState => prevState.set(key, value))
	}
	
	const [editPost, isLoading] = useFetching(async (postId: number, post: PostRequest, files: Blob[]) => {
		try {
			const response = await PostService.editPost(postId, post, files)
			navigate(`/posts/${response.data.id}`)
			message.success(`Вы успешно отредактировали пост ${response.data.name}`)
		} catch (error) {
			message.error(`Ошибка создания публикации ${error}`)
		}
	})
	
	const [fetchPost, isPostLoading] = useFetching(async (id: number) => {
		try {
			const response = await PostService.getPostById(id)
			if (!localStorage.getItem('username')) {
				navigate(`/posts/${id}`)
				message.warning('Для редактирования публикации необходимо авторизоваться')
				return
			}
			if (localStorage.getItem('username') !== response.data.owner.username) {
				navigate(`/posts/${id}`)
				message.warning(`Вы не автор данной публикации и не можете её редактировать`)
			} else {
				setPost({
					name: response.data.name,
					description: response.data.description
				})
				
				const uploadFiles = await Promise.all(response.data.mediaIds.map(async id => {
					const imageUrl = `${API_URL}/posts/medias/${id}`
					try {
						const response = await fetch(imageUrl)
						if (response.status === 200 && response.headers.get('content-type') !== null) {
							const blob = await response.blob()
							const reader = new FileReader()
							reader.readAsDataURL(blob)
							
							const loadPromise = new Promise<string>((resolve, reject) => {
								reader.onload = () => resolve(reader.result as string)
								reader.onerror = error => reject(error)
							})
							
							const imageDataUrl = await loadPromise
							const croppedImageDataUrl = await getCroppedImage(imageDataUrl)
							
							const uploadFile: UploadFile = {
								uid: id.toString(),
								url: imageUrl,
								thumbUrl: croppedImageDataUrl,
								name: `image ${id}`
							}
							updateMap(uploadFile.uid, blob)
							return uploadFile
						}
					} catch (error) {
						console.error(`Error fetching image with ID ${id}:`, error)
					}
					return null
				}))
				setFileList(uploadFiles.filter(file => file !== null) as UploadFile[])
			}
		} catch (error) {
			message.error(`Ошибка получения поста ${error}`)
		}
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
		
		const files: Blob[] = fileList.map(file => {
			if (file.originFileObj) {
				return file.originFileObj as Blob
			} else {
				const blob = fileBlobMap.get(file.uid)
				if (blob) {
					return blob
				} else {
					console.error(`Blob not found for UID: ${file.uid}`)
					return null
				}
			}
		}).filter(file => file !== null) as Blob[]
		
		if (files.length !== fileList.length) {
			console.error('Files array length does not match fileList length')
			return
		}
		
		editPost(params.id, trimmedValues, files)
	}
	
	useEffect(() => {
		fetchPost(params.id)
	}, [params.id])
	
	const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setPost({ ...post, name: e.target.value })
	}
	
	const handleDescriptionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		setPost({ ...post, description: e.target.value })
	}
	return (
		<Flex justify='space-around' align='flex-start'>
			<div style={{ width: '50%' }}>
				<CreatePostFormPreview post={post} fileList={fileList} preloadBlobMap={fileBlobMap} />
			</div>
			<Affix style={{ maxWidth: '45%' }}>
				<div>
					{isPostLoading ? <Spin /> :
						<Form layout='vertical' name='post-form' onFinish={onFinish}>
							<Typography.Title level={4}>Редактировать публикацию</Typography.Title>
							<Form.Item
								name='name'
								label='Заголовок публикации'
								initialValue={post.name}
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
								initialValue={post.description}
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
					}
				</div>
			</Affix>
		</Flex>
	)
}

export default EditPostForm
