import { message, Steps, type UploadFile } from 'antd'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import '../LoginForm/LoginForm.css'
import type { RegistrationRequest } from '../../types/RegistrationRequest'
import RegisterFormFirstStep from '../RegisterFormSteps/RegisterFormFirstStep'
import RegisterFormSecondStep from '../RegisterFormSteps/RegisterFormSecondStep'

const { Step } = Steps

// Объект, который мы получаем в конце первого этапа регистрации
interface FirstStepSlice {
	username: string
	email: string
	password: string
	confirmPassword: string
	secretWord: string
}

// Объект, который мы получаем в конце второго этапа регистрации
interface SecondStepSlice {
	fullName: string
	country: string
	city: string
	description: string
	avatarFile: UploadFile
}

const RegisterForm: React.FC = () => {
	const navigate = useNavigate()
	const [currentStep, setCurrentStep] = useState<number>(0)
	const [avatar, setAvatar] = useState<UploadFile>({} as UploadFile)
	const [firstStepData, setFirstStepData] = useState<FirstStepSlice>({} as FirstStepSlice)
	const [secondStepData, setSecondStepData] = useState<SecondStepSlice>({} as SecondStepSlice)
	
	
	const nextStep = () => {
		setCurrentStep(currentStep + 1)
	}
	
	const onFinishStep1 = (values: FirstStepSlice) => {
		if (values.password !== values.confirmPassword) {
			message.error('Пароли не совпадают')
			return
		}
		console.log('Step 1 values:', values)
		setFirstStepData(values)
		nextStep()
	}
	
	const onFinishStep2 = (values: SecondStepSlice) => {
		if (Object.keys(avatar).length === 0) {
			message.error('Выберите аватар')
			return
		}
		console.log('Step 2 values:', values)
		setSecondStepData(values)
		const registerRequest: RegistrationRequest = { ...firstStepData, ...values, avatarFile: avatar }
		// Здесь можно добавить логику отправки данных на сервер
		message.success('Вы успешно зарегистрировались')
		console.log(registerRequest)
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
				<RegisterFormSecondStep onFinishStep2={onFinishStep2} setAvatar={setAvatar} />
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
