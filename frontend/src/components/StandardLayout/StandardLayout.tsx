import { LoginOutlined } from '@ant-design/icons'
import { Button, Layout, Typography } from 'antd'
import React, { type FC, type PropsWithChildren } from 'react'
import Footer from '../Footer/Footer'
import SearchInput from '../SearchInput/SearchInput'

const { Header, Content } = Layout


interface LayoutProps extends PropsWithChildren {

}

const StandardLayout: FC<LayoutProps> = ({ children }) => {
	return (
		<Layout style={{ minHeight: '100vh' }}>
			<Header
				style={{ display: 'flex', alignItems: 'center', backgroundColor: 'white' }}>
				<Typography.Title className='artfolio-logo'>Artfolio</Typography.Title>
				<SearchInput />
				<Button icon={<LoginOutlined />} size='large'>Войти</Button>
			</Header>
			<Content style={{ padding: '0 48px' }}>
				{children}
			</Content>
			<Footer />
		</Layout>
	)
}

export default StandardLayout
