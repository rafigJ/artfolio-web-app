import { PlusOutlined } from '@ant-design/icons'
import type { GetProp, UploadFile, UploadProps } from 'antd'
import { message, Upload } from 'antd'
import React from 'react'

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

const beforeUpload = (file: FileType) => {
	const isPNG = file.type === 'image/png'
	const isJPG = file.type === 'image/jpg' || file.type === 'image/jpeg'
	if (!isPNG && !isJPG) {
		message.error(`${file.name} is not a png/jpg file`)
		return Upload.LIST_IGNORE
	}
	const isLt2M = file.size / 1024 / 1024 < 2
	if (!isLt2M) {
		message.error('Image must smaller than 2MB!')
		return Upload.LIST_IGNORE
	}
	return false
}

interface RegisterFormAvatarUploadProps {
	avatar: UploadFile[]
	setAvatar: React.Dispatch<React.SetStateAction<UploadFile[]>>
	setBlob?: React.Dispatch<React.SetStateAction<Blob | undefined>>
}

const RegisterFormAvatarUpload: React.FC<RegisterFormAvatarUploadProps> = ({ avatar, setAvatar, setBlob }) => {
	
	const uploadButton = (
		<button style={{ border: 0, background: 'none' }} type='button'>
			<PlusOutlined />
			<div style={{ marginTop: 8 }}>Загрузить</div>
		</button>
	)
	
	const onChange: UploadProps['onChange'] = ({ fileList }) => {
		setAvatar(fileList)
		if (setBlob !== undefined) {
			setBlob(undefined)
		}
	}
	
	return (
		<div style={{ margin: '10px 0' }}>
			<Upload
				name='avatar'
				listType='picture-circle'
				className='avatar-uploader'
				showUploadList={{ showDownloadIcon: false, showPreviewIcon: false }}
				beforeUpload={beforeUpload}
				fileList={avatar}
				onChange={onChange}
				maxCount={1}
			>
				{uploadButton}
			</Upload>
		</div>
	)
}

export default RegisterFormAvatarUpload