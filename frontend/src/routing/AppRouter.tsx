import React from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import CreatePostPage from '../pages/CreatePostPage/CreatePostPage'
import LoginPage from '../pages/LoginPage/LoginPage'
import MainPage from '../pages/MainPage/MainPage'
import ForgotPasswordPage from '../pages/ForgotPasswordPage/ForgotPasswordPage'
import RegisterPage from '../pages/RegisterPage/RegisterPage'

const AppRouter = () => {
	return (
		<Routes>
			<Route index path='/' element={<MainPage />} />
			<Route index path='/login' element={<LoginPage />} />
			<Route index path='forgotpassword' element={<ForgotPasswordPage />} />
			<Route index path='/posts/create' element={<CreatePostPage />} />
			<Route index path='/register' element={<RegisterPage/>} />
			<Route path='*' element={<Navigate replace to='/' />} />
		</Routes>
	)
}

export default AppRouter