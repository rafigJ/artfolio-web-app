import { EnvironmentOutlined, MailOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, message, Typography, UploadFile } from 'antd'
import { useContext, useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import UserService from '../../api/UserService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import { EditProfileRequest } from '../../types/user/EditProfileRequest'
import { FullUserResponse } from '../../types/user/FullUserResponse'
import RegisterFormAvatarUpload from '../RegisterFormAvatarUpload/RegisterFormAvatarUpload'
import './EditProfileForm.css'

const EditProfileForm = () => {
	
	const [prevAvatar, setPrevAvatar] = useState<Blob | undefined>()
	const [avatar, setAvatar] = useState<UploadFile[]>([] as UploadFile[])
	const [form] = Form.useForm()
	const navigate = useNavigate()
	
	const { authCredential, setAuthCredential } = useContext(AuthContext)
	const [profile, setProfile] = useState<FullUserResponse>({} as FullUserResponse)
	
	const [fetchUser] = useFetching(async (username) => {
		const response = await UserService.getUserByUsername(username)
		setProfile(response.data)
		
		const avatarUrl = `${API_URL}/user/${response.data.username}/avatar`
		
		try {
			const response = await fetch(avatarUrl)
			if (response.status === 200 && response.headers.get('content-type') !== null) {
				const blob = await response.blob()
				
				const uploadFile: UploadFile = {
					uid: username,
					url: avatarUrl,
					thumbUrl: avatarUrl,
					name: 'avatarSomeNameBig'
				}
				setPrevAvatar(blob)
				setAvatar([uploadFile])
			}
		} catch (error) {
			console.error(`Error fetching avatar: `, error)
		}
	})
	
	useEffect(() => {
		fetchUser(authCredential.username)
	}, [authCredential.username])
	
	useEffect(() => {
		form.setFieldsValue(profile)
	}, [profile, form])
	
	const onFinish = async (values: any) => {
		const editProfileRequest: EditProfileRequest = {
			username: values.username,
			email: values.email,
			fullName: values.fullName.trim(),
			country: values.country ? values.country.trim() : '',
			city: values.city ? values.city.trim() : '',
			description: values.description ? values.description.trim() : ''
		}
		try {
			const originFileObj = avatar.pop()?.originFileObj
			const currentAvatar = originFileObj ? originFileObj : prevAvatar
			const response = await UserService.editUserProfile(editProfileRequest, currentAvatar)
			setAuthCredential({
				username: response.data.username,
				email: response.data.email,
				name: response.data.fullName,
				token: authCredential.token,
				role: authCredential.role
			})
			message.success('Профиль успешно обновлён')
			navigate(`/profile/${response.data.username}`)
		} catch (error) {
			message.error('Произошла ошибка при редактировании профиля')
		}
	}
	
	return (
		<div className='edit-form-container'>
			<Form
				form={form}
				name='edit_profile'
				className='edit-form'
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
						{ max: 150, message: 'Логин должен содержать не более 150 символов' },
						{ min: 5, message: 'Логин должен содержать не менее 5 символов' },
						{
							pattern: /^[a-zA-Zа-яА-ЯёЁ0-9_]+$/,
							message: 'Логин не должен содержать служебные символы!'
						},
						{
							pattern: /^[^\u0400-\u04FFёЁ]+$/,
							message: 'Логин не должен содержать кириллицу!'
						}
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
					           { max: 40, message: 'Имя должно содержать не более 40 символов' },
					           {
						           validator: (_, value) => {
							           if (value.trim().replace(/\s/g, '').length < 3) {
								           return Promise.reject('Имя должно быть не меньше 3 символов')
							           }
							           if (value.trim().split(/\s+/).length > 2) {
								           return Promise.reject('Имя не может содержать более двух слов!')
							           }
							           return Promise.resolve()
						           }
					           }
				           ]}
				>
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
					           { max: 40, message: 'Название страны должно содержать не более 40 символов' },
					           {
						           validator: (_, value) => {
							           if (value.trim().split(/\s+/).length > 3) {
								           return Promise.reject('Название страны не может содержать более трёх слов!')
							           }
							           return Promise.resolve()
						           }
					           }
				           ]}
				>
					<Input
						prefix={<EnvironmentOutlined className='site-form-item-icon' />}
						placeholder='Страна'
					/>
				</Form.Item>
				
				<Form.Item name='city'
				           rules={[
					           { max: 40, message: 'Название города должно содержать не более 40 символов' },
					           {
						           validator: (_, value) => {
							           if (value.trim().split(/\s+/).length > 3) {
								           return Promise.reject('Название города не может содержать более трёх слов!')
							           }
							           return Promise.resolve()
						           }
					           }
				           ]}
				>
					<Input
						prefix={<EnvironmentOutlined className='site-form-item-icon' />}
						placeholder='Город'
					/>
				</Form.Item>
				
				<Form.Item name='description'>
					<Input.TextArea
						placeholder='Описание профиля'
						rows={4}
						maxLength={400}
						showCount
					/>
				</Form.Item>
				
				Фото профиля:
				<RegisterFormAvatarUpload avatar={avatar} setAvatar={setAvatar} setBlob={setPrevAvatar} />
				
				<Form.Item>
					<Button
						type='primary'
						htmlType='submit'
						className='edit-form-button'
					>
						Сохранить изменения
					</Button>
					<Link to={`/profile/${profile.username}`}>Назад к профилю</Link>
				</Form.Item>
			</Form>
		</div>
	)
}

export default EditProfileForm
