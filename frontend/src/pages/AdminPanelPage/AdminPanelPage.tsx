import { Tabs, Typography } from 'antd'
import { FC } from 'react'
import ReportTableComments from '../../components/ReportTable/ReportTableComments'
import ReportTablePost from '../../components/ReportTable/ReportTablePost'

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
						label: 'Жалобы на публикации',
						children: <ReportTablePost />,
					},
					{
						key: '2',
						label: 'Жалобы на комментарии',
						children: <ReportTableComments />,
					},
				]}
			/>
		</>
	)
}

export default AdminPanelPage