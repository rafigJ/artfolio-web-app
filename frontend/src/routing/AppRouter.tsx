import React from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import CreatePostPage from '../pages/CreatePostPage/CreatePostPage'
import LoginPage from '../pages/LoginPage/LoginPage'
import MainPage from '../pages/MainPage/MainPage'

const AppRouter = () => {
	return (
		<Routes>
			<Route index path='/' element={<MainPage />} />
			<Route index path='/login' element={<LoginPage />} />
			<Route index path='/posts/create' element={<CreatePostPage />} />
			<Route path='*' element={<Navigate replace to='/' />} />
		</Routes>
	)
}

export default AppRouter