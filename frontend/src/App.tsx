import './styles/App.css'
import React from 'react'
import { BrowserRouter } from 'react-router-dom'
import AppRouter from './routing/AppRouter'

const App: React.FC = () => {
	return (
		<BrowserRouter>
			<AppRouter />
		</BrowserRouter>
	)
}

export default App