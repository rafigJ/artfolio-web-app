import { InboxOutlined } from '@ant-design/icons'
import { DndContext, type DragEndEvent, PointerSensor, useSensor } from '@dnd-kit/core'
import { arrayMove, SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable'
import { Upload, type UploadFile, type UploadProps } from 'antd'
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
	
	return (
		<DndContext sensors={[sensor]} onDragEnd={onDragEnd}>
			<SortableContext items={fileList.map((i) => i.uid)} strategy={verticalListSortingStrategy}>
				<Upload.Dragger
					beforeUpload={() => false}
					fileList={fileList}
					onChange={onChange}
					listType='picture'
					itemRender={(originNode, file) => (
						<DraggableUploadListItem originNode={originNode} file={file} />
					)}
				>
					<p className='ant-upload-drag-icon'>
						<InboxOutlined />
					</p>
					<p className='ant-upload-text'>Click or drag file to this area to upload</p>
				</Upload.Dragger>
			</SortableContext>
		</DndContext>
	)
}

export default DraggableUploadList