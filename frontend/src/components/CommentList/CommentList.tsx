import { Comment } from '@ant-design/compatible'
import { List, Typography } from 'antd'
import React, { type FC } from 'react'

export interface CommentItem {
	author: string;
	avatar: string;
	content: React.ReactNode;
	datetime: React.ReactNode;
}

interface CommentListProps {
	data: CommentItem[]
}

const CommentList: FC<CommentListProps> = ({ data }) => {
	return (
		<List
			style={{ backgroundColor: 'transparent' }}
			header={<Typography.Title level={5}> Комментарии </Typography.Title>}
			itemLayout='horizontal'
			dataSource={data}
			renderItem={item => (
				<li>
					<Comment
						style={{ backgroundColor: 'transparent' }}
						author={item.author}
						avatar={item.avatar}
						content={item.content}
						datetime={item.datetime}
					/>
				</li>
			)}
		/>
	)
}

export default CommentList