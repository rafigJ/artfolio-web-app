import './styles/App.css'
import React from 'react'
import { BrowserRouter } from 'react-router-dom'
import StandardLayout from './components/StandardLayout/StandardLayout'
import AppRouter from './routing/AppRouter'

const App: React.FC = () => {
	return (
		<BrowserRouter>
			<StandardLayout>
				<AppRouter />
			</StandardLayout>
		</BrowserRouter>
	)
}

export default App