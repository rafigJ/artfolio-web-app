import { Button } from 'antd'
import React from 'react'
import { useNavigate } from 'react-router-dom'
import PostGrid from '../../components/PostGrid/PostGrid'

const MainPage = () => {
	const navigate = useNavigate()
	
	return (
		<>
			<PostGrid/>
		</>
	)
}

export default MainPage