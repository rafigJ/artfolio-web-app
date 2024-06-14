import { EnvironmentOutlined, MailOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography, UploadFile, message } from 'antd'
import { useContext, useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import UserService from '../../api/UserService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import { EditProfileRequest } from '../../types/user/EditProfileRequest'
import { FullUserResponse } from '../../types/user/FullUserResponse'
import './EditProfileForm.css'
import RegisterFormAvatarUpload from '../RegisterFormAvatarUpload/RegisterFormAvatarUpload'

const EditProfileForm = () => {
	const [avatar, setAvatar] = useState<UploadFile[]>([] as UploadFile[])
	const [form] = Form.useForm()
	const navigate = useNavigate()

	const { authCredential } = useContext(AuthContext)
	const [profile, setProfile] = useState<FullUserResponse>({} as FullUserResponse)

	const [fetchUser] = useFetching(async (username) => {
		const response = await UserService.getUserByUsername(username)
		setProfile(response.data)
	})

	useEffect(() => {
		fetchUser(authCredential.username)
	}, [authCredential.username])

	useEffect(() => {
		form.setFieldsValue(profile)
	}, [profile, form])

	const onFinish = async (values: any) => {
		if (!avatar.length) {
			message.error('Выберите аватар')
			return
		}
		const editProfileRequest: EditProfileRequest = {
			username: values.username,
			email: values.email,
			fullName: values.fullName.trim(),
			country: values.country ? values.country.trim() : '',
			city: values.city ? values.city.trim() : '',
			description: values.description ? values.description.trim() : '',
		}
		try {
			UserService.editUserProfile(editProfileRequest, avatar.pop()?.originFileObj)
			message.success("Профиль успешно обновлён")
			navigate(`/profile/${profile.username}`)
		}
		catch (error) {
			message.error("Произошла ошибка при редактировании профиля")
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
						},
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

				<Form.Item name='description'
					rules={[
						{ max: 400, message: 'Описание профиля должно содержать не более 400 символов' }
					]}
				>
					<Input.TextArea placeholder='Описание профиля' rows={4} />
				</Form.Item>

				Фото профиля:
				<RegisterFormAvatarUpload avatar={avatar} setAvatar={setAvatar} />

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
