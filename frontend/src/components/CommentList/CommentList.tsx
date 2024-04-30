import { Comment } from '@ant-design/compatible'
import { DeleteOutlined, EllipsisOutlined, FlagFilled } from '@ant-design/icons'
import { Dropdown, List, MenuProps, Typography } from 'antd'
import React, { useState, type FC } from 'react'
import ReportWindow from '../ReportWindow/ReportWindow'
import './CommentList.css'

export interface CommentItem {
	id: number
	author: string
	avatar: string
	content: React.ReactNode
	datetime: React.ReactNode
}

interface CommentListProps {
	data: CommentItem[]
}

const CommentList: FC<CommentListProps> = ({ data }) => {
	const [activeComment, setActiveComment] = useState<number | null>(null)

	const handleCommentHover = (id: number) => {
		setActiveComment(id)
	}

	const handleCommentLeave = () => {
		setActiveComment(null)
	}

	const [open, setOpen] = useState(false)

	const showModal = () => {
		setOpen(true)
	}

	const items: MenuProps['items'] = [
		{
			key: '1',
			label: 'Удалить',
			icon: <DeleteOutlined />,
		},
		{
			key: '2',
			onClick: showModal,
			label: 'Пожаловаться',
			icon: <FlagFilled color='red' />,
			danger: true,
		},
	]

	return (
		<><ReportWindow open={open} setOpen={setOpen} /><List
			style={{ backgroundColor: 'transparent' }}
			header={<Typography.Title level={5}> Комментарии </Typography.Title>}
			itemLayout='horizontal'
			dataSource={data}
			renderItem={item => (
				<li
					onMouseEnter={() => handleCommentHover(item.id)}
					onMouseLeave={handleCommentLeave}
				>
					<div className='comment-container'>
						<Comment
							style={{ backgroundColor: 'transparent' }}
							author={item.author}
							avatar={item.avatar}
							content={item.content}
							datetime={item.datetime} />
						<Dropdown menu={{ items }} placement='bottomLeft' arrow>
							<EllipsisOutlined
								className={activeComment === item.id ? 'menu-icon' : 'menu-icon-disable'} />
						</Dropdown>
					</div>
				</li>
			)} /></>
	)
}

export default CommentList
