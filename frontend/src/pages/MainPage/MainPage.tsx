import { Typography } from 'antd'
import React from 'react'
import PostGrid from '../../components/PostGrid/PostGrid'
import StandardLayout from '../../components/StandardLayout/StandardLayout'

const MainPage = () => {
	return (
		<StandardLayout>
			<Typography.Text>
				Content Main
			</Typography.Text>
			<PostGrid/>
		</StandardLayout>
	)
}

export default MainPage