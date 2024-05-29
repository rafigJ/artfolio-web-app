import { EnvironmentOutlined, MailOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography, UploadFile, message } from 'antd'
import { useState } from 'react'
import UserService from '../../api/UserService'
import { EditProfileRequest } from '../../types/EditProfileRequest'
import '../LoginForm/LoginForm.css'
import RegisterFormAvatarUpload from '../RegisterFormAvatarUpload/RegisterFormAvatarUpload'

const EditProfileForm = () => {
	const [avatar, setAvatar] = useState<UploadFile[]>([] as UploadFile[])

	const onFinish = async (values: any) => {
		if (!avatar.length) {
			message.error('Выберите аватар')
			return
		}
		const editProfileRequest: EditProfileRequest = values
		UserService.editUserProfile(editProfileRequest, avatar.pop()?.originFileObj)
	}

	const handleAvatarChange = (info: any) => {
		setAvatar(info)
	}

	return (
		<div className='login-form-container'>
			<Form
				name='edit_profile'
				className='login-form'
				initialValues={{ remember: true }}
				onFinish={onFinish}
			>
				<Typography.Title
					style={{ margin: '0 0 22px 0' }}
					level={3}
					className='login-title'
				>
					Редактирование профиля
				</Typography.Title>

				<Form.Item
					name='username'
					rules={[
						{ required: true, message: 'Введите логин!' },
						{ min: 5, message: 'Логин должен быть не меньше 5 символов!' },
						{ max: 150, message: 'Логин должен содержать не более 150 символов' }
					]}
				>
					<Input
						prefix={<UserOutlined className='site-form-item-icon' />}
						placeholder='Логин'
						autoComplete='off'
					/>
				</Form.Item>

				<Form.Item name='fullName'
					rules={[
						{ required: true, message: 'Введите имя!' },
						{ min: 3, message: 'Имя должно быть не меньше 3 символов!' },
						{ max: 40, message: 'Имя должно содержать не более 40 символов' }
					]}>
					<Input
						prefix={<UserOutlined className='site-form-item-icon' />}
						placeholder='Полное имя'
					/>
				</Form.Item>

				<Form.Item
					name='email'
					rules={[
						{ required: true, message: 'Введите электронную почту!' },
						{ type: 'email', message: 'Введите корректный адрес электронной почты!' },
						{ max: 150, message: 'Электронная почта должна содержать не более 150 символов' }
					]}
				>
					<Input
						prefix={<MailOutlined className='site-form-item-icon' />}
						placeholder='Электронная почта'
					/>
				</Form.Item>

				<Form.Item name='country'
					rules={[
						{ max: 40, message: 'Название страны должно содержать не более 40 символов' }
					]}>
					<Input
						prefix={<EnvironmentOutlined className='site-form-item-icon' />}
						placeholder='Страна'
					/>
				</Form.Item>

				<Form.Item name='city'
					rules={[
						{ max: 40, message: 'Название города должно содержать не более 40 символов' }
					]}>
					<Input
						prefix={<EnvironmentOutlined className='site-form-item-icon' />}
						placeholder='Город'
					/>
				</Form.Item>

				<Form.Item name='description'
					rules={[
						{ max: 400, message: 'Описание профиля должно содержать не более 400 символов' }
					]}>
					<Input.TextArea placeholder='Описание профиля' rows={4} />
				</Form.Item>

				Фото профиля:
				<RegisterFormAvatarUpload avatar={avatar} setAvatar={setAvatar} />

				<Form.Item>
					<Button
						type='primary'
						htmlType='submit'
						className='login-form-button'
					>
						Обновить профиль
					</Button>
				</Form.Item>
			</Form>
		</div>
	)
}

export default EditProfileForm
