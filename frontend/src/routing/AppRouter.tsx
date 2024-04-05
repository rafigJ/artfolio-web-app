import React from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import LoginPage from '../pages/LoginPage/LoginPage'
import MainPage from '../pages/MainPage/MainPage'

const AppRouter = () => {
	return (
		<Routes>
			<Route index path='/' element={<MainPage />} />
			<Route index path='/login' element={<LoginPage />} />
			<Route path='*' element={<Navigate replace to='/' />} />
		</Routes>
	)
}

export default AppRouter