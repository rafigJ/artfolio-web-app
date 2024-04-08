import { List, Tabs } from 'antd'
import React, { type FC } from 'react'
import PostCard from '../../components/PostCard/PostCard'
import ProfileDescription from '../../components/ProfileDescription/ProfileDescription'
import ProfileHeader from '../../components/ProfileHeader/ProfileHeader'
import type { MockFullUserResponse } from '../../types/MockTypes/MockFullUserResponse'
import type { Product } from '../../types/MockTypes/Product'

interface ProfilePostGridProps {
	data: Product[]
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
				xxl: 4
			}}
			dataSource={data}
			renderItem={(item) => (
				<List.Item>
					<PostCard key={item.id} product={item} />
				</List.Item>
			)}
		/>
	)
}

const ProfilePage = () => {
	const mockProfile: MockFullUserResponse = {
		'fullName': 'Рамси Болтон',
		'description': 'Креативный маркетолог с фокусом на цифровом маркетинге и контенте. Стратегически мыслющий профессионал с опытом в разработке и внедрении инновационных маркетинговых кампаний. Владею широким спектром навыков, включая аналитику, копирайтинг, дизайн и управление сообществом. Успешно создавал и продвигал бренды в различных секторах, обеспечивая рост продаж и увеличение онлайн-присутствия. Гибкость и способность адаптироваться к быстро меняющимся трендам помогают мне эффективно достигать поставленных целей. Я стремлюсь к постоянному развитию и готов принимать вызовы в динамичной среде цифрового маркетинга.',
		'country': 'Россия',
		'city': 'Уфа',
		'username': 'boltonArts',
		'email': 'bolton@vesteros.com',
		'postCount': 0,
		'subscribersCount': 3,
		'likeCount': 499
	}
	const data: Product[] = [
		{
			'albumId': 1,
			'id': 1,
			'title': 'accusamus beatae ad facilis cum similique qui sunt',
			'url': 'https://via.placeholder.com/600/92c952',
			'thumbnailUrl': 'https://via.placeholder.com/150/92c952'
		},
		{
			'albumId': 1,
			'id': 2,
			'title': 'reprehenderit est deserunt velit ipsam',
			'url': 'https://via.placeholder.com/600/771796',
			'thumbnailUrl': 'https://via.placeholder.com/150/771796'
		},
		{
			'albumId': 1,
			'id': 3,
			'title': 'officia porro iure quia iusto qui ipsa ut modi',
			'url': 'https://via.placeholder.com/600/24f355',
			'thumbnailUrl': 'https://via.placeholder.com/150/24f355'
		},
		{
			'albumId': 1,
			'id': 4,
			'title': 'culpa odio esse rerum omnis laboriosam voluptate repudiandae',
			'url': 'https://via.placeholder.com/600/d32776',
			'thumbnailUrl': 'https://via.placeholder.com/150/d32776'
		},
		{
			'albumId': 1,
			'id': 5,
			'title': 'natus nisi omnis corporis facere molestiae rerum in',
			'url': 'https://via.placeholder.com/600/f66b97',
			'thumbnailUrl': 'https://via.placeholder.com/150/f66b97'
		},
		{
			'albumId': 1,
			'id': 6,
			'title': 'accusamus ea aliquid et amet sequi nemo',
			'url': 'https://via.placeholder.com/600/56a8c2',
			'thumbnailUrl': 'https://via.placeholder.com/150/56a8c2'
		}
	]
	
	return (
		<>
			<ProfileHeader />
			<Tabs
				defaultActiveKey='1'
				type='card'
				centered
				items={
					[{
						key: '1',
						label: 'Публикации',
						children: <ProfilePostGrid data={data} />
					},
						{
							key: '2',
							label: 'Описание профиля',
							children: <ProfileDescription profile={mockProfile} />
						}
					]}
			/>
		</>
	)
}

export default ProfilePage