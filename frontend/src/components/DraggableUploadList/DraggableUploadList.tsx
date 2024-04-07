import { InboxOutlined } from '@ant-design/icons'
import { DndContext, type DragEndEvent, PointerSensor, useSensor } from '@dnd-kit/core'
import { arrayMove, horizontalListSortingStrategy, SortableContext } from '@dnd-kit/sortable'
import { message, Upload, type UploadFile, type UploadProps } from 'antd'
import React, { useState } from 'react'
import DraggableUploadListItem from '../DraggableUploadListItem/DraggableUploadListItem'

const DraggableUploadList = () => {
	const [fileList, setFileList] = useState<UploadFile[]>([])
	
	const sensor = useSensor(PointerSensor, {
		activationConstraint: { distance: 10 }
	})
	
	const onDragEnd = ({ active, over }: DragEndEvent) => {
		if (active.id !== over?.id) {
			setFileList((prev) => {
				const activeIndex = prev.findIndex((i) => i.uid === active.id)
				const overIndex = prev.findIndex((i) => i.uid === over?.id)
				return arrayMove(prev, activeIndex, overIndex)
			})
		}
	}
	
	const onChange: UploadProps['onChange'] = ({ fileList: newFileList }) => {
		setFileList(newFileList)
	}
	
	const uploadProps: UploadProps = {
		beforeUpload: (file) => {
			const isPNG = file.type === 'image/png'
			const isJPG = file.type === 'image/jpg' || file.type === 'image/jpeg'
			if (!isPNG && !isJPG) {
				message.error(`${file.name} is not a png/jpg file`)
				return Upload.LIST_IGNORE
			}
			return false
		},
		maxCount: 10,
		multiple: true,
		listType: 'picture-card',
	}
	
	return (
		<DndContext sensors={[sensor]} onDragEnd={onDragEnd}>
			<SortableContext items={fileList.map((i) => i.uid)} strategy={horizontalListSortingStrategy}>
				<Upload.Dragger
					fileList={fileList}
					onChange={onChange}
					itemRender={(originNode, file) => (
						<DraggableUploadListItem originNode={originNode} file={file} />
					)}
					showUploadList={{ showPreviewIcon: false }}
					onPreview={() => {}}
					{...uploadProps}
				>
					<p className='ant-upload-drag-icon'>
						<InboxOutlined />
					</p>
					<p className='ant-upload-text'>Переместите изображения в эту область для их загрузки</p>
				</Upload.Dragger>
			</SortableContext>
		</DndContext>
	)
}

export default DraggableUploadList