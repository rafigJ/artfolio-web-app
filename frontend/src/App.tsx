import './styles/App.css'
import React from 'react'
import { BrowserRouter } from 'react-router-dom'
import AppRouter from './routing/AppRouter'
import StandardLayout from './components/StandardLayout/StandardLayout'

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