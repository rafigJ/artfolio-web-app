import { useContext, useEffect } from 'react'
import { Navigate, Route, Routes, useLocation } from 'react-router-dom'
import { AuthContext } from '../context'
import AdminPanelPage from '../pages/AdminPanelPage/AdminPanelPage'
import CreatePostPage from '../pages/CreatePostPage/CreatePostPage'
import EditProfilePage from '../pages/EditProfilePage/EditProfilePage'
import ForgotPasswordPage from '../pages/ForgotPasswordPage/ForgotPasswordPage'
import LoginPage from '../pages/LoginPage/LoginPage'
import MainPage from '../pages/MainPage/MainPage'
import PostPage from '../pages/PostPage/PostPage'
import ProfilePage from '../pages/ProfilePage/ProfilePage'
import RegisterPage from '../pages/RegisterPage/RegisterPage'
import SearchPage from '../pages/SearchPage/SearchPage'

const AppRouter = () => {
	const { isAuth, authCredential } = useContext(AuthContext)

	const location = useLocation()

	useEffect(() => {
		if (typeof window.ym === 'function') {
			window.ym(97163910, 'hit', location.pathname)
		}
	}, [location])


	return (
		<Routes>
			<Route index path='/' element={<MainPage />} />
			<Route index path='/search' element={<SearchPage />} />
			{!isAuth && <>
				<Route path='/login' element={<LoginPage />} />
				<Route path='/register' element={<RegisterPage />} />
				<Route path='/forgot-password' element={<ForgotPasswordPage />} />
			</>
			}
			<Route path='/posts/:id' element={<PostPage />} />
			<Route path='/profile/:username' element={<ProfilePage />} />
			{isAuth ?
				<Route path='/posts/create' element={<CreatePostPage />} />
				:
				<Route path='/posts/create' element={<Navigate replace to='/login' />} />
			}
			{isAuth ?
				<Route path='/profile/edit' element={<EditProfilePage />} />
				:
				<Route path='/profile/edit' element={<Navigate replace to='/login' />} />
			}
			{/* {isAuth && authCredential.role === 'ADMIN' && */}
			<Route path='/admin' element={<AdminPanelPage />} />
			{/* } */}
			<Route path='*' element={<Navigate replace to='/' />} />
		</Routes>
	)
}

export default AppRouter
