import { Input } from 'antd'
import { type FC } from 'react'
import './SearchInput.css'
import { useNavigate } from 'react-router-dom'

const { Search } = Input

const SearchInput: FC = () => {
	const navigate = useNavigate()
	return (
		<div className='header__search-wrapper'>
			<Search
				className='header__search-input'
				size='large'
				placeholder='Поиск...'
				allowClear
				onSearch={(value) => navigate('/search?name=' + value)}
			/>
		</div>
	)
}

export default SearchInput
