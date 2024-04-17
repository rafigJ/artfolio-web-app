import { Comment } from '@ant-design/compatible'
import { DeleteOutlined, EllipsisOutlined, FlagFilled } from '@ant-design/icons'
import { Dropdown, List, MenuProps, Typography } from 'antd'
import React, { type FC } from 'react'
import './CommentList.css'

export interface CommentItem {
	author: string;
	avatar: string;
	content: React.ReactNode;
	datetime: React.ReactNode;
}

interface CommentListProps {
	data: CommentItem[]
}

const items: MenuProps['items'] = [
	{
		key: '1',
		label: ('Удалить'),
		icon: <DeleteOutlined />
	},
	{
		key: '2',
		label: ('Пожаловаться'),
		icon: <FlagFilled color='red' />,
		danger: true
	}
]

const CommentList: FC<CommentListProps> = ({ data }) => {
	return (
		<List
			style={{ backgroundColor: 'transparent' }}
			header={<Typography.Title level={5}> Комментарии </Typography.Title>}
			itemLayout='horizontal'
			dataSource={data}
			renderItem={item => (
				<li>
					<div className='comment-container'>
						<Comment
							style={{ backgroundColor: 'transparent' }}
							author={item.author}
							avatar={item.avatar}
							content={item.content}
							datetime={item.datetime}
						/>
						<Dropdown menu={{ items }} placement='bottomLeft' arrow>
							<EllipsisOutlined className='menu-icon' />
						</Dropdown>
					</div>
				</li>
			)}
		/>
	)
}

export default CommentList