import type { DropdownProps, MenuProps } from 'antd'
import { Dropdown } from 'antd'
import { type FC, useContext, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import { AuthContext } from '../../context'
import { AuthResponse } from '../../types/auth/AuthResponse'
import './HeaderProfileMenu.css'

const HeaderProfileMenu: FC = () => {
	const [open, setOpen] = useState(false)
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
			key: '3',
			danger: true
		}
	]
	
	useEffect(() => {
		if (authCredential?.username) {
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
