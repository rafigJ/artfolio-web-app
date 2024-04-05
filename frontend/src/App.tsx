import './styles/App.css'
import React from 'react'
import StandardLayout from './components/StandardLayout/StandardLayout'

const App: React.FC = () => {
	return (
		<StandardLayout>
			<div
				style={{
					padding: 24
				}}
			>
				Contentsadas
			</div>
		</StandardLayout>
	)
}

export default App