import { LoginOutlined, PlusOutlined } from '@ant-design/icons'
import { Button, Layout, Typography } from 'antd'
import { useNavigate } from 'react-router-dom'
import SearchInput from '../SearchInput/SearchInput'

const Header = () => {
	const navigate = useNavigate()

	return (
		<Layout.Header
			style={{
				display: 'flex',
				alignItems: 'center',
				backgroundColor: 'white',
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
			<Button
				icon={<LoginOutlined />}
				size='large'
				onClick={() => navigate('/login')}
			>
				Войти
			</Button>
		</Layout.Header>
	)
}

export default Header
