import { PlusOutlined } from '@ant-design/icons'
import type { GetProp, UploadFile, UploadProps } from 'antd'
import { message, Upload } from 'antd'
import React, { useState } from 'react'

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

const beforeUpload = (file: FileType) => {
	const isPNG = file.type === 'image/png'
	const isJPG = file.type === 'image/jpg' || file.type === 'image/jpeg'
	if (!isPNG && !isJPG) {
		message.error(`${file.name} is not a png/jpg file`)
		return Upload.LIST_IGNORE
	}
	const isLt2M = file.size / 1024 / 1024 < 2;
	if (!isLt2M) {
		message.error('Image must smaller than 2MB!');
		return Upload.LIST_IGNORE
	}
	return false
}


const RegisterFormAvatarUpload: React.FC = () => {
	const [avatar, setAvatar] = useState<UploadFile>({} as UploadFile)
	
	const uploadButton = (
		<button style={{ border: 0, background: 'none' }} type='button'>
			<PlusOutlined />
			<div style={{ marginTop: 8 }}>Загрузить</div>
		</button>
	)
	
	const onChange: UploadProps['onChange'] = ({ file }) => {
		if (file) {
			setAvatar(file)
			console.log(file)
		}
	}
	
	return (
		<div style={{ margin: '10px 0' }}>
			<Upload
				name='avatar'
				listType='picture-circle'
				className='avatar-uploader'
				showUploadList={{ showRemoveIcon: false, showDownloadIcon: false, showPreviewIcon: false }}
				beforeUpload={beforeUpload}
				onChange={onChange}
				maxCount={1}
			>
				{uploadButton}
			</Upload>
		</div>
	)
}

export default RegisterFormAvatarUpload