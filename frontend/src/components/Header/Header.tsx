import { LoginOutlined, PlusOutlined } from '@ant-design/icons'
import { Avatar, Button, Layout, Typography } from 'antd'
import { useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import { AuthContext } from '../../context'
import SearchInput from '../SearchInput/SearchInput'

const Header = () => {
	const navigate = useNavigate()
	const { isAuth, setIsAuth } = useContext(AuthContext)
	
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
			{isAuth ?
				<Avatar size={48} alt='Аватар пользователя' src={<img alt='Аватар пользователя' style={{ width: '48px', height: '48px' }}
				                            src={`${API_URL}/user/designer18234/avatar`} />} />
				:
				<Button
					icon={<LoginOutlined />}
					size='large'
					onClick={() => navigate('/login')}
				>
					Войти
				</Button>
			}
		</Layout.Header>
	)
}

export default Header
