import { LoginOutlined, PlusOutlined } from '@ant-design/icons'
import { Button, Layout, Typography } from 'antd'
import { ReactNode, useContext, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AuthContext } from '../../context'
import HeaderProfileMenu from '../HeaderProfileMenu/HeaderProfileMenu'
import SearchInput from '../SearchInput/SearchInput'

const Header = () => {
	const navigate = useNavigate()
	const { isAuth, authCredential } = useContext(AuthContext)
	const [profileMenu, setProfileMenu] = useState<ReactNode>()
	
	useEffect(() => {
		const profileMenu = isAuth ?
			<HeaderProfileMenu />
			:
			<Button
				icon={<LoginOutlined />}
				size='large'
				onClick={() => navigate('/login')}
			>
				Войти
			</Button>
		setProfileMenu(profileMenu)
	}, [isAuth, authCredential])
	
	return (
		<Layout.Header
			style={{
				display: 'flex',
				alignItems: 'center',
				backgroundColor: 'white'
			}}
		>
			<Typography.Title
				className='artfolio-logo'
				style={{ cursor: 'pointer' }}
				onClick={() => navigate('/')}
			>
				Artfolio
			</Typography.Title>
			<SearchInput />
			<Button
				icon={<PlusOutlined />}
				size='large'
				type='primary'
				style={{ marginRight: '20px' }}
				onClick={() => navigate('/posts/create')}
			>
				Опубликовать
			</Button>
			{profileMenu}
		</Layout.Header>
	)
}

export default Header
