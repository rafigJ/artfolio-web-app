import { useSortable } from '@dnd-kit/sortable'
import { CSS } from '@dnd-kit/utilities'
import type { UploadFile } from 'antd'
import React from 'react'

interface DraggableUploadListItemProps {
	originNode: React.ReactElement<any, string | React.JSXElementConstructor<any>>
	file: UploadFile
}
/**
 * Используется в DraggableUploadList
 * Взято из документации antd из компонента Upload
 */
const DraggableUploadListItem = ({
	originNode,
	file,
}: DraggableUploadListItemProps) => {
	const {
		attributes,
		listeners,
		setNodeRef,
		transform,
		transition,
		isDragging,
	} = useSortable({
		id: file.uid,
	})

	const style: React.CSSProperties = {
		transform: CSS.Transform.toString(transform),
		transition,
		cursor: 'move',
	}

	return (
		<div
			ref={setNodeRef}
			style={style}
			// prevent preview event when drag end
			className={isDragging ? 'is-dragging' : ''}
			{...attributes}
			{...listeners}
		>
			{/* hide error tooltip when dragging */}
			{file.status === 'error' && isDragging
				? originNode.props.children
				: originNode}
		</div>
	)
}

export default DraggableUploadListItem
