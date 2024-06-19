import { Divider, Typography } from 'antd'
import { UploadFile } from 'antd/lib/upload/interface'
import { FC, useEffect, useRef, useState } from 'react'
import { useObjectUrls } from '../../hooks/useObjectUrls'
import type { PostRequest } from '../../types/post/PostRequest'

interface CreatePostFormPreviewProps {
	post: PostRequest
	fileList: UploadFile[]
	preloadBlobMap?: Map<string, Blob>
}

/**
 * Компонент CreatePostFormPreview используется в форме создания поста.
 * Этот компонент динамически отображает заголовок, изображения и описание создаваемой публикации.
 * @param post Объект, передаваемый в запрос.
 * @param fileList Список файлов изображений, которые отображаются на странице.
 * @param preloadBlobList
 */
const CreatePostFormPreview: FC<CreatePostFormPreviewProps> = ({
	                                                               post,
	                                                               fileList,
	                                                               preloadBlobMap = new Map<string, Blob>()
                                                               }) => {
	const [objectURLs, setObjectURLs] = useState<string[]>([])
	const cachedMap = useRef<Map<string, string>>(new Map<string, string>())
	
	
	const getUrl = useObjectUrls()
	useEffect(() => {
		
		const getObjectURLs = async () => {
			const map = cachedMap.current
			preloadBlobMap.forEach((blob, key) => {
				if (!map.has(key)) {
					const cachedUrl = URL.createObjectURL(blob)
					map.set(key, cachedUrl)
				}
			})
			const newObjectURLs: string[] = []
			for (const file of fileList) {
				if (file.originFileObj) {
					const objectURL = getUrl(file.originFileObj)
					newObjectURLs.push(objectURL)
				} else if (file.url) {
					const newVar = map.get(file.uid)
					if (newVar) {
						newObjectURLs.push(newVar)
					}
				}
			}
			setObjectURLs(newObjectURLs)
		}
		getObjectURLs()
	}, [fileList])
	
	return (
		<div style={{ display: 'flex', flexDirection: 'column' }}>
			<Typography.Title level={3}>{post.name}</Typography.Title>
			{objectURLs.map((objectURL, index) => (
				<div key={index}>
					<img
						src={objectURL}
						alt={fileList[index]?.name}
						style={{ marginTop: '10px', maxWidth: '100%' }}
					/>
				</div>
			))}
			<Divider>Описание</Divider>
			<Typography.Text>{post.description}</Typography.Text>
		</div>
	)
}

export default CreatePostFormPreview
