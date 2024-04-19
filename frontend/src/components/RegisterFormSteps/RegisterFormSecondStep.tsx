import { EnvironmentOutlined, UserOutlined } from '@ant-design/icons'
import { Button, Form, Input, Typography } from 'antd'
import React, { type FC } from 'react'
import RegisterFormAvatarUpload from '../RegisterFormAvatarUpload/RegisterFormAvatarUpload'

interface RegisterFormSecondStep {
	onFinishStep2: ((values: any) => void) | undefined
}

const RegisterFormSecondStep: FC<RegisterFormSecondStep> = ({ onFinishStep2 }) => {
	
	const normFile = (e: any) => {
		if (Array.isArray(e)) {
			return e
		}
		return e?.fileList
	}
	
	return (
		<div className='description-step'>
			<Form
				name='register_step_2'
				className='login-form'
				initialValues={{ remember: true }}
				onFinish={onFinishStep2}
			>
				<Typography.Title level={3} className='login-title'>
					Регистрация
				</Typography.Title>
				
				<Form.Item name='fullName'>
					<Input
						prefix={<UserOutlined className='site-form-item-icon' />}
						placeholder='Полное имя'
					/>
				</Form.Item>
				
				<Form.Item name='country'>
					<Input
						prefix={<EnvironmentOutlined className='site-form-item-icon' />}
						placeholder='Страна'
					/>
				</Form.Item>
				
				<Form.Item name='city'>
					<Input
						prefix={<EnvironmentOutlined className='site-form-item-icon' />}
						placeholder='Город'
					/>
				</Form.Item>
				
				Фото профиля:
				<RegisterFormAvatarUpload />
				
				
				<Form.Item name='description'>
					<Input.TextArea placeholder='Описание профиля' rows={4} />
				</Form.Item>
				<Form.Item>
					<Button
						type='primary'
						htmlType='submit'
						className='login-form-button'
					>
						Зарегистрироваться
					</Button>
				</Form.Item>
			</Form>
		</div>
	)
}

export default RegisterFormSecondStep