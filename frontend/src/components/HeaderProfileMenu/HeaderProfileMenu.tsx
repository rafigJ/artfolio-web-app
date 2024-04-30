import type { DropdownProps, MenuProps } from 'antd'
import { Dropdown } from 'antd'
import './HeaderProfileMenu.css'
import React, { type FC, useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AuthContext } from '../../context'

interface HeaderProfileMenuProps {
	src: string
}

const HeaderProfileMenu: FC<HeaderProfileMenuProps> = ({ src }) => {
	const [open, setOpen] = useState(false)
	const { setIsAuth, authCredential } = useContext(AuthContext)
	const navigate = useNavigate()
	const handleMenuClick: MenuProps['onClick'] = (e) => {
		if (e.key === '1') {
			navigate('/')
		}
		if (e.key === '2') {
		
		}
		if (e.key === '3') {
			localStorage.removeItem('token')
			navigate('/')
			setIsAuth(false)
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
	
	return (
		<Dropdown
			menu={{
				items,
				onClick: handleMenuClick
			}}
			onOpenChange={handleOpenChange}
			open={open}
		>
			<img className='header-avatar__image' width={32} height={32} alt='Аватар пользователя' src={src} />
		</Dropdown>
	)
}

export default HeaderProfileMenu