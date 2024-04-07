import { Divider, Typography } from 'antd'
import { UploadFile } from 'antd/lib/upload/interface'
import React, { FC, useEffect, useState } from 'react'
import type { MockPostRequest } from '../../types/MockTypes/MockPostRequest'

interface CreatePostFormPreviewProps {
	post: MockPostRequest;
	fileList: UploadFile[];
}

/**
 * Функция для получения временного URL изображения из файла.
 * @param file Файл изображения.
 * @return Promise<string> Возвращает Promise с временным URL изображения.
 */
const getObjectURLFromFile = (file: File): Promise<string> => {
	return new Promise<string>((resolve) => {
		const objectURL = URL.createObjectURL(file);
		resolve(objectURL);
	});
};

/**
 * Компонент CreatePostFormPreview используется в форме создания поста.
 * Этот компонент динамически отображает заголовок, изображения и описание создаваемой публикации.
 * @param post Объект, передаваемый в запрос.
 * @param fileList Список файлов изображений, которые отображаются на странице.
 */
const CreatePostFormPreview: FC<CreatePostFormPreviewProps> = ({ post, fileList }) => {
	const [objectURLs, setObjectURLs] = useState<string[]>([]);
	
	useEffect(() => {
		const getObjectURLs = async () => {
			const newObjectURLs: string[] = [];
			for (const file of fileList) {
				if (file.originFileObj) {
					const objectURL = await getObjectURLFromFile(file.originFileObj);
					newObjectURLs.push(objectURL);
				}
			}
			setObjectURLs(newObjectURLs);
		};
		
		getObjectURLs();
	}, [fileList]);
	
	return (
		<div style={{ display: 'flex', flexDirection: 'column' }}>
			<Typography.Title level={3}>
				{post.title}
			</Typography.Title>
			{objectURLs.map((objectURL, index) => (
				<div key={index}>
					<img
						src={objectURL}
						alt={fileList[index]?.name}
						style={{ marginTop: '10px', maxWidth: '100%' }} />
				</div>
			))}
			<Divider>Описание</Divider>
			<Typography.Text>
				{post.description}
			</Typography.Text>
		</div>
	);
};

export default CreatePostFormPreview;
