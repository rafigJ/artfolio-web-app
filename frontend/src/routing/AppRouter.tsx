import { Navigate, Route, Routes } from 'react-router-dom'
import AdminPanelPage from '../pages/AdminPanelPage/AdminPanelPage'
import CreatePostPage from '../pages/CreatePostPage/CreatePostPage'
import ForgotPasswordPage from '../pages/ForgotPasswordPage/ForgotPasswordPage'
import LoginPage from '../pages/LoginPage/LoginPage'
import MainPage from '../pages/MainPage/MainPage'
import PostPage from '../pages/PostPage/PostPage'
import ProfilePage from '../pages/ProfilePage/ProfilePage'
import RegisterPage from '../pages/RegisterPage/RegisterPage'

const AppRouter = () => {
	return (
		<Routes>
			<Route index path='/' element={<MainPage />} />
			<Route path='/login' element={<LoginPage />} />
			<Route path='/forgot-password' element={<ForgotPasswordPage />} />
			<Route path='/posts/create' element={<CreatePostPage />} />
			<Route path='/register' element={<RegisterPage />} />
			<Route path='/posts/:id' element={<PostPage />} />
			<Route path='/profile/:username' element={<ProfilePage />} />
			<Route path='/admin' element={<AdminPanelPage />} />
			<Route path='*' element={<Navigate replace to='/' />} />
		</Routes>
	)
}

export default AppRouter
