import { Layout } from 'antd'
import React, { type FC, type PropsWithChildren } from 'react'
import Footer from '../Footer/Footer'
import Header from '../Header/Header'


const { Content } = Layout

interface LayoutProps extends PropsWithChildren {

}

const StandardLayout: FC<LayoutProps> = ({ children }) => {
	return (
		<Layout style={{ minHeight: '100vh' }}>
			<Header />
			<Content style={{ padding: '0 48px' }}>
				<div
					style={{
						padding: 24
					}}
				>
					{children}
				</div>
			</Content>
			<Footer />
		</Layout>
	)
}

export default StandardLayout
