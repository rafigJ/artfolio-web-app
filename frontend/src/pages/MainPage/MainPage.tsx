import { Button } from 'antd'
import React from 'react'
import { useNavigate } from 'react-router-dom'
import StandardLayout from '../../components/StandardLayout/StandardLayout'

const MainPage = () => {
	const navigate = useNavigate()
	
	return (
		<StandardLayout>
			<Button size='large' type='primary' onClick={() => navigate('/posts/create')}>Создать пост</Button>
		</StandardLayout>
	)
}

export default MainPage