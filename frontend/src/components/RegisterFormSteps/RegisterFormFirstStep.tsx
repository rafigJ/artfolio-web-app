import { LockOutlined, MailOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography } from 'antd'
import React, { type FC } from 'react'
import { Link } from 'react-router-dom'

interface RegisterFormFirstStep {
	onFinishStep1: ((values: any) => void) | undefined
}

const RegisterFormFirstStep:FC<RegisterFormFirstStep> = ({onFinishStep1}) => {
	
	return (
		<div className='login-form-container'>
			<Form
				name='register_step_1'
				className='login-form'
				initialValues={{ remember: true }}
				onFinish={onFinishStep1}
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
					rules={[{ required: true, message: 'Введите логин!' }]}
					initialValue='designer1823'
				>
					<Input
						prefix={<UserOutlined className='site-form-item-icon' />}
						placeholder='Логин'
					/>
				</Form.Item>
				<Form.Item
					name='email'
					rules={[
						{ required: true, message: 'Введите электронную почту!' }
					]}
					initialValue='designer1823@mail.ru'
				>
					<Input
						prefix={<MailOutlined className='site-form-item-icon' />}
						placeholder='Электронная почта'
					/>
				</Form.Item>
				<Form.Item
					name='password'
					rules={[{ required: true, message: 'Введите пароль!' }]}
					initialValue='somePassword18'
				>
					<Input.Password
						prefix={<LockOutlined className='site-form-item-icon' />}
						type='password'
						placeholder='Пароль'
					/>
				</Form.Item>
				
				<Form.Item
					name='confirmPassword'
					rules={[{ required: true, message: 'Повторите пароль!' }]}
					initialValue='somePassword18'
				>
					<Input.Password
						prefix={<LockOutlined className='site-form-item-icon' />}
						type='password'
						placeholder='Повторите пароль'
					/>
				</Form.Item>
				<Form.Item
					name='secretWord'
					rules={[{ required: true, message: 'Введите секретное слово!' }]}
					initialValue='someSecret'
				>
					<Input
						prefix={<LockOutlined className='site-form-item-icon' />}
						placeholder='Секретное слово'
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