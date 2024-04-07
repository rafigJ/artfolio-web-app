import { Flex } from 'antd'
import React from 'react'
import type { Product } from '../../types/MockTypes/Product'
import CreatePostFormPreview from '../CreatePostFormPreview/CreatePostFormPreview'
import DraggableUploadList from '../DraggableUploadList/DraggableUploadList'

const CreatePostForm = () => {
	const post: Product = {
		'albumId': 1,
		'id': 41,
		'title': 'Райан Гослинг Красавчик',
		'url': 'https://upload.wikimedia.org/wikipedia/commons/3/36/Ryan_Gosling_%2835397111013%29.jpg',
		'thumbnailUrl': 'https://upload.wikimedia.org/wikipedia/commons/3/36/Ryan_Gosling_%2835397111013%29.jpg'
	}
	
	return (
		<Flex justify='space-around' align='flex-start'>
			<CreatePostFormPreview post={post} />
			<div style={{maxWidth: '50%'}}>
				<DraggableUploadList />
			</div>
		</Flex>
	)
}

export default CreatePostForm