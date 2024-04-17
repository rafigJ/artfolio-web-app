import React, { type FC } from 'react'
import { Layout, Typography } from 'antd'

const Footer: FC = () => {
	return (<Layout.Footer style={{ display: 'flex' }}>
		<Typography.Title className='artfolio-logo' level={3} style={{
			alignSelf: 'flex-start',
			padding: '0',
			margin: '0 20px 0 0'
		}}>Artfolio</Typography.Title>
		<div style={{ display: 'flex', flexDirection: 'column' }}>
			Artfolio App ©{new Date().getFullYear()}
			<span>По всем вопросам обращаться по почте <a
				href='mailto:rafigdzabbarov0410@gmail.com'>rafigdzabbarov0410@gmail.com</a></span>
		</div>
	</Layout.Footer>)
}

export default Footer