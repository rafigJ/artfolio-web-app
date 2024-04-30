import { Input } from 'antd'
import type { SearchProps } from 'antd/es/input/Search'
import { type FC } from 'react'
import './SearchInput.css'

const { Search } = Input

const onSearch: SearchProps['onSearch'] = (value, _e, info) =>
	console.log(info?.source, value)

const SearchInput: FC = () => {
	return (
		<div className='header__search-wrapper'>
			<Search
				className='header__search-input'
				size='large'
				placeholder='Поиск...'
				allowClear
				onSearch={onSearch}
			/>
		</div>
	)
}

export default SearchInput
