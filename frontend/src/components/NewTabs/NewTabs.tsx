import { Tabs } from 'antd'
import { FC } from 'react'

interface NewTabsProps {
	label: string[]
	onChange: (activeKey: string) => void
}

const NewTabs: FC<NewTabsProps> = ({ label, onChange }) => {
	return (
		<Tabs
			defaultActiveKey='0'
			type='card'
			size='large'
			onChange={onChange}
			items={label.map((lab, index) => {
				return {
					label: lab,
					key: index.toString()
				}
			})}
		/>
	)
}

export default NewTabs
