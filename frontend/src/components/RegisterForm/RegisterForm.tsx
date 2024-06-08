import { message, Steps, type UploadFile } from 'antd'
import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import AuthService from '../../api/AuthService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import type { RegistrationRequest } from '../../types/RegistrationRequest'
import '../LoginForm/LoginForm.css'
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
	avatarFile: UploadFile[]
}

const RegisterForm: React.FC = () => {
	const navigate = useNavigate()
	const [currentStep, setCurrentStep] = useState<number>(0)
	const [avatar, setAvatar] = useState<UploadFile[]>([] as UploadFile[])
	const [firstStepData, setFirstStepData] = useState<FirstStepSlice>({} as FirstStepSlice)
	const [secondStepData, setSecondStepData] = useState<SecondStepSlice>({} as SecondStepSlice)
	const { setAuthCredential, setIsAuth } = useContext(AuthContext)

	const [register, isLoading, isError, error] = useFetching(async (registerRequest: RegistrationRequest, avatar: any) => {
		const response = await AuthService.register(registerRequest, avatar)
		setAuthCredential(response.data)
		localStorage.setItem('token', response.data.token)
		setIsAuth(true)
		message.success((`Вы успешно зарегистрировались ${response.status}`))
	})

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
		window.ym(97163910, 'reachGoal', 'step1Success')
		nextStep()
	}

	const onFinishStep2 = (values: SecondStepSlice) => {
		if (!avatar.length) {
			message.error('Выберите аватар')
			return
		}
		const trimmedValues: SecondStepSlice = {
			fullName: values.fullName.trim(),
			country: values.country ? values.country.trim() : '',
			city: values.city ? values.city.trim() : '',
			description: values.description ? values.description.trim() : '',
			avatarFile: values.avatarFile
		}
		console.log('Step 2 values:', trimmedValues)
		setSecondStepData(trimmedValues)
		const registerRequest: RegistrationRequest = { ...firstStepData, ...trimmedValues }
		console.log(registerRequest)
		register(registerRequest, avatar.pop()?.originFileObj)
		window.ym(97163910, 'reachGoal', 'step2Success')
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
				<RegisterFormSecondStep onFinishStep2={onFinishStep2} avatar={avatar} setAvatar={setAvatar} />
			)
		}
	]
	// todo перенести в fetch
	if (!isLoading && isError) {
		message.error(`Ошибка регистрации ${error}`)
	}

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
