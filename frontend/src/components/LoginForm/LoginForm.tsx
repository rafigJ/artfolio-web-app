import { LockOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography } from 'antd'
import './LoginFormStyles.css'
import { Link } from 'react-router-dom'

const LoginForm = () => {
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
				<Typography.Title style={{ margin: '0 0 22px 0' }} level={3} className='login-title'>
					Войти в Artfolio
				</Typography.Title>
				<Form.Item
					name='email'
					rules={[{ required: true, message: 'Введите электронную почту!' }]}
				>
					<Input prefix={<UserOutlined className='site-form-item-icon' />} placeholder='Электронная почта' />
				</Form.Item>
				
				<Form.Item
					name='password'
					rules={[{ required: true, message: 'Введите пароль!' }]}
				>
					<Input.Password
						prefix={<LockOutlined className='site-form-item-icon' />}
						type='password'
						placeholder='Пароль'
					/>
				</Form.Item>
				<Form.Item style={{ margin: '5px 0' }}>
					<Link className='login-form-forgot' to='/forgot-password'>
						Забыли пароль
					</Link>
				</Form.Item>
				<Form.Item>
					<Button type='primary' htmlType='submit' className='login-form-button'>
						Войти
					</Button>
					У вас нет аккаунта? <Link to='/register'>Зарегистрироваться!</Link>
				</Form.Item>
			</Form>
		</div>
	
	)
}

export default LoginForm