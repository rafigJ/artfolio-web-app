import { Comment } from '@ant-design/compatible'
import { List, Tooltip, Typography } from 'antd'
import React from 'react'

const data = [
	{
		author: 'Han Solo',
		avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=13',
		content: (
			<p>
				We supply a series of design principles, practical patterns and high quality design
				resources (Sketch and Axure), to help people create their product prototypes beautifully and
				efficiently.
			</p>
		),
		datetime: (
			<Tooltip title='2016-11-22 11:22:33'>
				<span>8 hours ago</span>
			</Tooltip>
		)
	},
	{
		author: 'Han Solo',
		avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=1',
		content: (
			<p>
				We supply a series of design principles, practical patterns and high quality design
				resources (Sketch and Axure), to help people create their product prototypes beautifully and
				efficiently.
			</p>
		),
		datetime: (
			<Tooltip title='2016-11-22 10:22:33'>
				<span>9 hours ago</span>
			</Tooltip>
		)
	}
]
const CommentList = () => {
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