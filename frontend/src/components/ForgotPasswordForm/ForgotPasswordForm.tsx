import { LockOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography, message } from 'antd'
import { useNavigate } from 'react-router-dom'
import $api from '../../api'
import '../LoginForm/LoginForm.css'

const ForgotPasswordform = () => {
	const navigate = useNavigate()
	const onFinish = async (values: any) => {
		try {
			const response = await $api.patch('auth/change-password', {
				email: values.email,
				secretWord: values.secretWord,
				newPassword: values.newPassword,
			})

			if (response.status === 200) {
				message.success('Пароль успешно изменен!')
				navigate('/login')
			} else {
				message.error('Произошла ошибка при сбросе пароля')
			}
		} catch (error) {
			message.error('Произошла ошибка при сбросе пароля')
		}
	}

	return (
		<div className='login-form-container'>
			<Form
				name='normal_login'
				className='login-form'
				initialValues={{ remember: true }}
				onFinish={onFinish}
			>
				<Typography.Title
					style={{ margin: '0 0 22px 0' }}
					level={3}
					className='login-title'
				>
					Восстановление пароля
				</Typography.Title>
				<Form.Item
					name='email'
					rules={[
						{ required: true, message: 'Введите электронную почту!' },
						{ type: 'email', message: 'Введите корректный адрес электронной почты!' },
						{ max: 150, message: 'Электронная почта должна содержать не более 150 символов' }
					]}
				>
					<Input
						prefix={<UserOutlined className='site-form-item-icon' />}
						placeholder='Электронная почта'
					/>
				</Form.Item>

				<Form.Item
					name='secretWord'
					rules={[{ required: true, message: 'Введите секретное слово!' }]}
				>
					<Input
						prefix={<LockOutlined className='site-form-item-icon' />}
						placeholder='Секретное слово'
					/>
				</Form.Item>

				<Form.Item
					name='newPassword'
					rules={[
						{ required: true, message: 'Введите пароль!' },
						{
							pattern: /^(?=.*[A-Za-z])(?=.*\d).{8,}$/,
							message: 'Пароль должен содержать минимум 8 символов, хотя бы одну букву и цифру'
						},
						{
							pattern: /^[a-zA-Zа-яА-ЯёЁ0-9]+$/,
							message: 'Пароль не должен содержать служебные символы!'
						},
						{
							pattern: /^[^\u0400-\u04FFёЁ]+$/,
							message: 'Пароль не должен содержать кириллицу!'
						},
					]}
				>
					<Input.Password
						prefix={<LockOutlined className='site-form-item-icon' />}
						type='password'
						placeholder='Новый пароль'
					/>
				</Form.Item>
				<Form.Item>
					<Button
						type='primary'
						htmlType='submit'
						className='login-form-button'
					>
						Сбросить пароль
					</Button>
				</Form.Item>
			</Form>
		</div>
	)
}

export default ForgotPasswordform
