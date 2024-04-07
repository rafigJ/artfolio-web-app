import { Divider, Typography } from 'antd'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import type { MockFullPostResponse } from '../../types/MockTypes/MockFullPostResponse'

const PostContent = () => {
	const params = useParams()
	const [post, setPost] = useState<MockFullPostResponse>({
		'id': 33,
		'title': 'Sternwarte Darmstadt HDR Panorama 10MB',
		'description': 'Dieses Bild zeigt den Ausblick von der Sternwarte in Richtung Darmstadt, ' +
			'Frankfurt ist ebenfalls hinter Darmstadt zu sehen. Das Bild ist ein HDR (High-Dynamic-Range) Panorama, und setzt sich aus mehr als 20 Einzelbildern zusammen. Die Farbgebung ist künstlerisch ausgearbeitet, alle Details sind allerdings zu 100% der Realität entsprechend, eine Komposition oder Retusche hat nicht stattgefunden.',
		'likeCount': 39,
		'previewMedia': 949,
		'medias': [
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg',
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg',
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg',
			'https://upload.wikimedia.org/wikipedia/commons/9/9b/Sternwarte_Darmstadt_HDR_Panorama_10MB_-_Photographed_by_James_Breitenstein.jpg'
		],
		'owner': {
			'fullName': 'James Breitenstein',
			'username': 'james54'
		}
	})
	
	useEffect(() => console.log(params.id), [])
	return (
		<div style={{ display: 'flex', flexDirection: 'column', maxWidth: '80%'}}>
			<Typography.Title level={3}>
				{post.title}
			</Typography.Title>
			{post.medias.map((media, index) => (
				<div key={index}>
					<img
						src={media}
						alt={index.toString()}
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

export default PostContent