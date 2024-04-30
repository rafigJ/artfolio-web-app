import { Tabs, Typography } from 'antd'
import { FC } from 'react'
import ReportTable from '../../components/ReportTable/ReportTable'

const AdminPanelPage: FC = () => {
	return (
		<><Typography.Title level={3}>
			Панель администратора
		</Typography.Title>
			<Tabs
				defaultActiveKey='1'
				items={[
					{
						key: '1',
						label: 'Нерассмотренные',
						children: <ReportTable getChecked={false} />,
					},
					{
						key: '2',
						label: 'Рассмотренные',
						children: <ReportTable getChecked={true} />,
					},
				]}
			/></>
	)
}

export default AdminPanelPage