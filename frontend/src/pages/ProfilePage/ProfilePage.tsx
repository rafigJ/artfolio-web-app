import { List, Tabs } from 'antd'
import { type FC } from 'react'
import PostCard from '../../components/PostCard/PostCard'
import ProfileDescription from '../../components/ProfileDescription/ProfileDescription'
import ProfileHeader from '../../components/ProfileHeader/ProfileHeader'
import type { MockFullUserResponse } from '../../types/MockTypes/MockFullUserResponse'
import type { Product } from '../../types/MockTypes/Product'
import type { PostResponse } from '../../types/PostResponse'

interface ProfilePostGridProps {
	data: PostResponse[]
}

const ProfilePostGrid: FC<ProfilePostGridProps> = ({ data }) => {
	return (
		<List
			grid={{
				gutter: 16,
				xs: 1,
				sm: 2,
				md: 3,
				lg: 4,
				xl: 4,
				xxl: 4,
			}}
			dataSource={data}
			renderItem={item => (
				<List.Item>
					<PostCard key={item.id} post={item} />
				</List.Item>
			)}
		/>
	)
}

const ProfilePage = () => {
	const mockProfile: MockFullUserResponse = {
		fullName: 'Рамси Болтон',
		description:
			'Креативный маркетолог с фокусом на цифровом маркетинге и контенте. Стратегически мыслющий профессионал с опытом в разработке и внедрении инновационных маркетинговых кампаний. Владею широким спектром навыков, включая аналитику, копирайтинг, дизайн и управление сообществом. Успешно создавал и продвигал бренды в различных секторах, обеспечивая рост продаж и увеличение онлайн-присутствия. Гибкость и способность адаптироваться к быстро меняющимся трендам помогают мне эффективно достигать поставленных целей. Я стремлюсь к постоянному развитию и готов принимать вызовы в динамичной среде цифрового маркетинга.',
		country: 'Россия',
		city: 'Уфа',
		username: 'boltonArts',
		email: 'bolton@vesteros.com',
		postCount: 0,
		subscribersCount: 3,
		likeCount: 499,
	}
	const data: PostResponse[] = []

	return (
		<>
			<ProfileHeader />
			<Tabs
				defaultActiveKey='1'
				centered
				items={[
					{
						key: '1',
						label: 'Публикации',
						children: <ProfilePostGrid data={data} />,
					},
					{
						key: '2',
						label: 'Описание профиля',
						children: <ProfileDescription profile={mockProfile} />,
					},
				]}
			/>
		</>
	)
}

export default ProfilePage
