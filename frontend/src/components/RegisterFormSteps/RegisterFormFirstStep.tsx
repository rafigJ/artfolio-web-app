import { LockOutlined, MailOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography } from 'antd'
import { type FC } from 'react'
import { Link } from 'react-router-dom'

interface RegisterFormFirstStep {
	onFinishStep1: ((values: any) => void) | undefined
}

const RegisterFormFirstStep: FC<RegisterFormFirstStep> = ({ onFinishStep1 }) => {

	return (
		<div className='login-form-container'>
			<Form
				name='register_step_1'
				className='login-form'
				initialValues={{ username: '', password: '' }}
				onFinish={onFinishStep1}
				autoComplete='off'
			>
				<Typography.Title
					style={{ margin: '0 0 22px 0' }}
					level={3}
					className='login-title'
				>
					Регистрация
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
				<Form.Item
					name='password'
					rules={[
						{ required: true, message: 'Введите пароль!' },
						{
							pattern: /^(?=.*[A-Za-z])(?=.*\d).{8,}$/,
							message: 'Пароль должен содержать минимум 8 символов, хотя бы одну букву и цифру'
						},
						{
							pattern: /^[A-Za-z\d]+$/,
							message: 'Пароль не должен содержать служебные символы!'
						}
					]}
				>
					<Input.Password
						prefix={<LockOutlined className='site-form-item-icon' />}
						type='password'
						placeholder='Пароль'
						autoComplete='off'
					/>
				</Form.Item>

				<Form.Item
					name='confirmPassword'
					rules={[{ required: true, message: 'Повторите пароль!' }]}
				>
					<Input.Password
						prefix={<LockOutlined className='site-form-item-icon' />}
						type='password'
						placeholder='Повторите пароль'
					/>
				</Form.Item>
				<Form.Item
					name='secretWord'
					rules={[{ required: true, message: 'Введите секретное слово!' },
					{ min: 5, message: 'Секретное слово должно быть не меньше 5 символов!' }
					]}
				>
					<Input
						prefix={<LockOutlined className='site-form-item-icon' />}
						placeholder='Секретное слово'
						autoComplete='off'
					/>
				</Form.Item>

				<Form.Item>
					<Button
						type='primary'
						htmlType='submit'
						className='login-form-button'
					>
						Продолжить
					</Button>
					У вас уже есть аккаунт? <Link to='/login'>Войти</Link>
				</Form.Item>
			</Form>
		</div>
	)
}

export default RegisterFormFirstStep