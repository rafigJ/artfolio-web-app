import { Button, Result } from 'antd'
import React from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import SearchPostGrid from '../../components/SearchPostGrid/SearchPostGrid'

function useQuery() {
	const { search } = useLocation()
	
	return React.useMemo(() => new URLSearchParams(search), [search])
}


const SearchPage = () => {
	const query = useQuery()
	const name = query.get('name')
	const navigate = useNavigate()
	if (name === null) {
		return <Result title='400'
		               subTitle='Query name is null'
		               extra={<Button onClick={() => navigate('/')}> Вернуться на главную </Button>}
		/>
	}
	return (
		<SearchPostGrid name={name} />
	)
}

export default SearchPage
