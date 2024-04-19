import { message, Steps } from 'antd'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import '../LoginForm/LoginForm.css'
import type { RegistrationRequest } from '../../types/RegistrationRequest'
import RegisterFormFirstStep from '../RegisterFormSteps/RegisterFormFirstStep'
import RegisterFormSecondStep from '../RegisterFormSteps/RegisterFormSecondStep'

const { Step } = Steps

const RegisterForm: React.FC = () => {
	const navigate = useNavigate()
	const [currentStep, setCurrentStep] = useState<number>(0)
	
	const nextStep = () => {
		setCurrentStep(currentStep + 1)
	}
	
	const onFinishStep1 = (values: any) => {
		if (values.password !== values.confirmPassword) {
			message.error('Пароли не совпадают')
			return
		}
		console.log('Step 1 values:', values)
		nextStep()
	}
	
	const onFinishStep2 = (values: any) => {
		console.log('Step 2 values:', values)
		// Здесь можно добавить логику отправки данных на сервер
		message.success('Вы успешно зарегистрировались')
		navigate('/')
	}
	
	const steps = [
		{
			title: 'Введите учётные данные',
			content: (
				<RegisterFormFirstStep onFinishStep1={onFinishStep1} />
			)
		},
		{
			title: 'Добавьте описание профиля',
			content: (
				<RegisterFormSecondStep onFinishStep2={onFinishStep2} />
			)
		}
	]
	
	return (
		<div className='steps'>
			<Steps current={currentStep}>
				{steps.map((item, index) => (
					<Step key={index} title={item.title} />
				))}
			</Steps>
			<div className='steps-content'>{steps[currentStep].content}</div>
		</div>
	)
}

export default RegisterForm
