import { Comment } from '@ant-design/compatible'
import { DeleteOutlined, EllipsisOutlined, FlagFilled } from '@ant-design/icons'
import { Avatar, Dropdown, List, MenuProps, message, Typography } from 'antd'
import React, { type FC, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { API_URL } from '../../api'
import AuthService from '../../api/AuthService'
import CommentService from '../../api/CommentService'
import { useFetching } from '../../hooks/useFetching'
import { CommentResponse } from '../../types/comment/CommentResponse'
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
	data: CommentResponse[]
	setData: React.Dispatch<React.SetStateAction<CommentResponse[]>>
}

const CommentList: FC<CommentListProps> = ({ data , setData}) => {
	const [activeComment, setActiveComment] = useState<number | null>(null)
	const navigate = useNavigate()
	
	const handleCommentHover = (id: number) => {
		setActiveComment(id)
	}
	
	const handleCommentLeave = () => {
		setActiveComment(null)
	}
	
	const [deleteComment, isLoading, isError, error] = useFetching(async (postId: number, deleteComment: CommentResponse) => {
		await CommentService.deleteComment(postId, deleteComment.id)
			.then(response => {
				data.filter(p => p.id !== deleteComment.id)
				message.success('Комментарий ' + deleteComment.comment + ' удален')
			})
			.catch((e) => message.error('Ошибка удаления комментария ' + e))
	})
	
	
	const [open, setOpen] = useState(false)
	
	const showModal = () => {
		setOpen(true)
	}

	return (
		<>
			<ReportWindow open={open} setOpen={setOpen} />
			<List
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
								author={<Link to={`/profile/${item.owner.username}`}>{item.owner.fullName} </Link>}
								avatar={<Avatar onClick={() => navigate(`/profile/${item.owner.username}`)}
								                src={`${API_URL}/user/${item.owner.username}/avatar`} />}
								content={item.comment}
								datetime={item.createTime} />
							<Dropdown menu={{ items: [
									{
										key: '1',
										label: 'Удалить',
										icon: <DeleteOutlined />,
										disabled: true,
										onClick: (item) => {
											console.log(item)
										}
									},
									{
										key: '2',
										onClick: showModal,
										label: 'Пожаловаться',
										icon: <FlagFilled color='red' />,
										danger: true
									}
								] }} placement='bottomLeft' arrow>
								<EllipsisOutlined
									className={activeComment === item.id ? 'menu-icon' : 'menu-icon-disable'} />
							</Dropdown>
						</div>
					</li>
				)} />
		</>
	)
}

export default CommentList
