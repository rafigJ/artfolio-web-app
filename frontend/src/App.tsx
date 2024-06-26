import React, { useEffect, useState } from 'react'
import { BrowserRouter } from 'react-router-dom'
import AuthService from './api/AuthService'
import StandardLayout from './components/StandardLayout/StandardLayout'
import { AuthContext } from './context'
import { useFetching } from './hooks/useFetching'
import AppRouter from './routing/AppRouter'
import './styles/App.css'
import type { AuthResponse } from './types/auth/AuthResponse'

const App: React.FC = () => {
	const [authCredential, setAuthCredential] = useState<AuthResponse>({} as AuthResponse)
	const [isAuth, setIsAuth] = useState<boolean>(false)
	
	const [fetchUser] = useFetching(async () => {
		await AuthService.userCredentials().then((response) => {
			setAuthCredential({ ...response.data })
			setIsAuth(true)
			localStorage.setItem('username', response.data.username)
		}).catch((reason: any) => {
				if (reason.response.status === 401) {
					localStorage.removeItem('token')
					localStorage.removeItem('username')
				}
			}
		)
	})
	
	useEffect(() => {
		const token = localStorage.getItem('token')
		if (token !== null) {
			fetchUser()
		}
	}, [])
	
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