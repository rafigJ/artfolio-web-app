import { Comment } from '@ant-design/compatible'
import { DeleteOutlined, EllipsisOutlined, FlagFilled } from '@ant-design/icons'
import { Avatar, Dropdown, List, Typography, message } from 'antd'
import moment from 'moment'
import 'moment/locale/ru'
import React, { useContext, useState, type FC } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { API_URL } from '../../api'
import CommentService from '../../api/CommentService'
import { AuthContext } from '../../context'
import { useFetching } from '../../hooks/useFetching'
import { CommentResponse } from '../../types/comment/CommentResponse'
import ReportCommentWindow from '../ReportWindow/ReportCommentWindow'
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

const CommentList: FC<CommentListProps> = ({ data, setData }) => {
	const [activeComment, setActiveComment] = useState<CommentResponse | null>(null)
	const [activeCommentId, setActiveCommentId] = useState<number>(0)
	const { authCredential, isAuth } = useContext(AuthContext)
	const navigate = useNavigate()
	const param = useParams()
	const handleCommentHover = (comment: CommentResponse) => {
		setActiveComment(comment)
		setActiveCommentId(comment.id)
	}

	const handleCommentLeave = () => {
		setActiveComment(null)
	}

	const [deleteComment] = useFetching(async (postId: number, deleteComment: CommentResponse) => {
		await CommentService.deleteComment(postId, deleteComment.id)
			.then(response => {
				setData(prevState =>
					prevState.filter(p => p.id !== deleteComment.id)
				)
				message.success('Комментарий ' + deleteComment.comment + ' удален!')
			})
			.catch((e) => message.error('Ошибка удаления комментария ' + e))
	})

	const [openCommentReport, setOpenCommentReport] = useState(false)

	const showCommentReport = () => {
		setOpenCommentReport(true)
	}

	const getMenuItems = (item: CommentResponse) => {
		const items = [
			{
				key: '2',
				onClick: showCommentReport,
				label: 'Пожаловаться',
				icon: <FlagFilled color='red' />,
				danger: true
			}
		]

		if (isAuth && (authCredential.role === 'ADMIN' || authCredential.username === item.owner.username)) {
			items.unshift({
				key: '1',
				label: 'Удалить',
				icon: <DeleteOutlined />,
				onClick: () => deleteComment(Number(param.id), item),
				danger: false
			})
		}

		return items
	}
	return (
		<>
			<ReportCommentWindow open={openCommentReport} setOpen={setOpenCommentReport} commentId={activeCommentId} />
			<List
				style={{ backgroundColor: 'transparent' }}
				header={<Typography.Title level={5}> Комментарии </Typography.Title>}
				itemLayout='horizontal'
				dataSource={data}
				renderItem={item => (
					<li
						onMouseEnter={() => handleCommentHover(item)}
						onMouseLeave={handleCommentLeave}
					>
						<div className='comment-container'>
							<Comment
								style={{ backgroundColor: 'transparent' }}
								author={<Link to={`/profile/${item.owner.username}`}>{item.owner.fullName} </Link>}
								avatar={<Avatar onClick={() => navigate(`/profile/${item.owner.username}`)}
									src={`${API_URL}/user/${item.owner.username}/avatar`} />}
								content={item.comment}
								datetime={moment(item.createTime).fromNow()} />
							<Dropdown menu={{
								items: getMenuItems(item)
							}} placement='bottomLeft' arrow>
								<EllipsisOutlined
									className={activeComment?.id === item.id ? 'menu-icon' : 'menu-icon-disable'} />
							</Dropdown>
						</div>
					</li>
				)} />
		</>
	)
}

export default CommentList
