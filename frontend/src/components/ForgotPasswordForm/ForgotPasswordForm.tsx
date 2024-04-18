import { LockOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography } from 'antd'
import '../LoginForm/LoginForm.css'

const ForgotPasswordform = () => {
	const onFinish = (values: any) => {
		console.log('Received values of form: ', values)
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
					rules={[{ required: true, message: 'Введите электронную почту!' }]}
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
					rules={[{ required: true, message: 'Введите новый пароль!' }]}
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
