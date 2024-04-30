import { Button, Result } from 'antd'
import React from 'react'
import { useNavigate } from 'react-router-dom'

const Error404Result = () => {
	const navigate = useNavigate()
	
	return (
		<Result status='404'
		        title='404'
		        subTitle='Страница не найдена'
		        extra={<Button onClick={() => navigate('/')}> Вернуться на главную </Button>}
		/>
	)
}

export default Error404Result