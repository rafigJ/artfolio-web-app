import { Divider, Typography, type UploadFile } from 'antd'
import { RcFile } from 'antd/es/upload'
import React, { FC, useEffect, useState } from 'react'
import type { MockPostRequest } from '../../types/MockTypes/MockPostRequest'

interface CreatePostFormPreviewProps {
	post: MockPostRequest;
	fileList: UploadFile[];
}

const getDataURLFromFile = (file: RcFile) => {
	return new Promise<string>((resolve, reject) => {
		const reader = new FileReader()
		reader.onload = () => {
			resolve(reader.result as string)
		}
		reader.onerror = reject
		reader.readAsDataURL(file)
	})
}

const CreatePostFormPreview: FC<CreatePostFormPreviewProps> = ({ post, fileList }) => {
	const [dataURLs, setDataURLs] = useState<string[]>([])
	
	const getDataURLs = async () => {
		const dataURLs: string[] = []
		for (const file of fileList) {
			if (file.originFileObj) {
				const dataURL = await getDataURLFromFile(file.originFileObj)
				dataURLs.push(dataURL)
			}
		}
		setDataURLs(dataURLs)
	}
	
	useEffect(() => {
		getDataURLs()
	}, [fileList])
	
	return (
		<div style={{ display: 'flex', flexDirection: 'column' }}>
			{/* <PostCard product={post} /> */}
			<Typography.Title level={3}>
				{post.title}
			</Typography.Title>
			{dataURLs.map((dataURL, index) => (
				<div>
					<img key={index}
					     src={dataURL}
					     alt={fileList[index]?.name}
					     style={{ marginTop: '10px', maxWidth: '100%' }} />
				</div>
			))}
			<Divider>Описание</Divider>
			<Typography.Text>
				{post.description}
			</Typography.Text>
		</div>
	)
}

export default CreatePostFormPreview
