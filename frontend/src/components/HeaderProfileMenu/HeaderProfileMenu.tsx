import type { DropdownProps, MenuProps } from 'antd'
import { Dropdown } from 'antd'
import { useContext, useEffect, useState, type FC } from 'react'
import { useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import { AuthContext } from '../../context'
import { AuthResponse } from '../../types/auth/AuthResponse'
import './HeaderProfileMenu.css'

const HeaderProfileMenu: FC = () => {
	const [open, setOpen] = useState(false)
	const [imageKey, setImageKey] = useState(0)
	const { setIsAuth, authCredential, setAuthCredential } = useContext(AuthContext)
	const navigate = useNavigate()

	const [imageSrc, setImageSrc] = useState(`${API_URL}/user/${authCredential?.username}/avatar`)

	const handleMenuClick: MenuProps['onClick'] = (e) => {
		if (e.key === '1') {
			navigate(`/profile/${authCredential?.username}`)
		}
		if (e.key === '2') {
			navigate(`/profile/edit`)
		}
		if (e.key === '3') {
			navigate(`/admin`)
		}
		if (e.key === '4') {
			localStorage.removeItem('token')
			localStorage.removeItem('username')
			navigate('/')
			setIsAuth(false)
			setAuthCredential({} as AuthResponse)
		}
		setOpen(false)
	}

	const handleOpenChange: DropdownProps['onOpenChange'] = (nextOpen, info) => {
		if (info.source === 'trigger' || nextOpen) {
			setOpen(nextOpen)
		}
	}

	const items: MenuProps['items'] = [
		{
			label: 'Профиль',
			key: '1'
		},
		{
			label: 'Редактирование профиля',
			key: '2'
		},
		{
			label: 'Выйти',
			key: '4',
			danger: true
		}
	]

	if (authCredential.role === 'ADMIN') {
		items.splice(2, 0,
			{
				label: 'Панель администратора',
				key: '3',
				danger: true,
			},
		)
	}


	useEffect(() => {
		if (authCredential?.username) {
			setImageKey(Math.random())
			setImageSrc(`${API_URL}/user/${authCredential?.username}/avatar`)
		}
	}, [authCredential])

	return (
		<Dropdown
			menu={{
				items,
				onClick: handleMenuClick
			}}
			onOpenChange={handleOpenChange}
			open={open}
		>
			<img
				key={imageKey}
				className='header-avatar__image'
				width={32}
				height={32}
				alt='Аватар пользователя'
				src={imageSrc}
			/>
		</Dropdown>
	)
}

export default HeaderProfileMenu
