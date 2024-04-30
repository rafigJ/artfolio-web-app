import { List, Skeleton, Tabs } from 'antd'
import { type FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import UserService from '../../api/UserService'
import Error404Result from '../../components/Error404Result/Error404Result'
import PostCard from '../../components/PostCard/PostCard'
import ProfileDescription from '../../components/ProfileDescription/ProfileDescription'
import ProfileHeader from '../../components/ProfileHeader/ProfileHeader'
import { useFetching } from '../../hooks/useFetching'
import type { FullUserResponse } from '../../types/FullUserResponse'
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
				xxl: 4
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
	const params = useParams()
	
	const [profile, setProfile] = useState<FullUserResponse>({} as FullUserResponse)
	
	const [fetchUser, isLoading, isError, error] = useFetching(async (username) => {
		const response = await UserService.getUserByUsername(username)
		setProfile(response.data)
	})
	
	const data: PostResponse[] = []
	
	useEffect(() => {
		fetchUser(params.username)
	}, [params.username])
	
	if (isError) {
		return <Error404Result />
	}
	
	return (
		<>
			{isLoading
				?
				<Skeleton />
				:
				<ProfileHeader profile={profile} />
			}
			<Tabs
				defaultActiveKey='1'
				centered
				items={[
					{
						key: '1',
						label: 'Публикации',
						children: <ProfilePostGrid data={data} />
					},
					{
						key: '2',
						label: 'Описание профиля',
						children: <ProfileDescription profile={profile} />
					}
				]}
			/>
		</>
	)
}

export default ProfilePage
