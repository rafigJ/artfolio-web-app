import './styles/App.css'
import React, { useEffect, useState } from 'react'
import { BrowserRouter } from 'react-router-dom'
import AuthService from './api/AuthService'
import StandardLayout from './components/StandardLayout/StandardLayout'
import { AuthContext } from './context'
import { useFetching } from './hooks/useFetching'
import AppRouter from './routing/AppRouter'
import type { AuthResponse } from './types/AuthResponse'

const App: React.FC = () => {
	const [authCredential, setAuthCredential] = useState<AuthResponse>({} as AuthResponse)
	const [isAuth, setIsAuth] = useState<boolean>(false)
	
	const [fetchUser, isError] = useFetching(async () => {
		const response = await AuthService.userCredentials()
		setAuthCredential({ ...response.data })
		setIsAuth(true)
	})
	
	useEffect(() => {
		const token = localStorage.getItem('token')
		if (token !== null) {
			console.log(token)
			fetchUser()
		}
	}, [isError])
	
	return (
		<AuthContext.Provider value={{
			authCredential,
			isAuth,
			setIsAuth,
			setAuthCredential
		}}>
			<BrowserRouter>
				<StandardLayout>
					<AppRouter />
				</StandardLayout>
			</BrowserRouter>
		</AuthContext.Provider>
	)
}

export default App