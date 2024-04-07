import { Typography } from 'antd'
import React from 'react'
import PostGrid from '../../components/PostGrid/PostGrid'
import StandardLayout from '../../components/StandardLayout/StandardLayout'
import NewTabs from '../../components/Tabs/NewTabs'

const MainPage = () => {
	return (
		<StandardLayout> 
			<NewTabs label={['Новые', 'Популярные', 'Для вас']}/>
		</StandardLayout>
	)
}

export default MainPage