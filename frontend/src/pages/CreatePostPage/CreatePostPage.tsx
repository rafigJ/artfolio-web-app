import React, { type FC } from 'react'
import CreatePostForm from '../../components/CreatePostForm/CreatePostForm'
import StandardLayout from '../../components/StandardLayout/StandardLayout'

const CreatePostPage: FC = () => {
	return (
		<StandardLayout>
			<CreatePostForm/>
		</StandardLayout>
	)
}

export default CreatePostPage