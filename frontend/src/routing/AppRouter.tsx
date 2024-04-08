import React from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import CreatePostPage from '../pages/CreatePostPage/CreatePostPage'
import LoginPage from '../pages/LoginPage/LoginPage'
import MainPage from '../pages/MainPage/MainPage'
import ForgotPasswordPage from '../pages/ForgotPasswordPage/ForgotPasswordPage'
import RegisterPage from '../pages/RegisterPage/RegisterPage'
import PostPage from '../pages/PostPage/PostPage'

const AppRouter = () => {
	return (
		<Routes>
			<Route index path='/' element={<MainPage />} />
			<Route path='/login' element={<LoginPage />} />
			<Route path='/forgot-password' element={<ForgotPasswordPage />} />
			<Route path='/posts/create' element={<CreatePostPage />} />
			<Route path='/register' element={<RegisterPage/>} />
      <Route path='/posts/:id' element={<PostPage />} />
			<Route path='*' element={<Navigate replace to='/' />} />
		</Routes>
	)
}

export default AppRouter