import type { AxiosResponse } from 'axios'
import { IPageResponse } from '../types/IPageResponse'
import { ReportRequest, ReportType } from '../types/reports/ReportRequest'
import { ReportResponse } from '../types/reports/ReportResponse'
import $api from './index'

export default class ReportService {

	static async getReports(type: ReportType,
		page: number = 0,
		limit: number = 10,
		reviewed: string): Promise<AxiosResponse<IPageResponse<ReportResponse>>> {
		return await $api.get(`/reports`, {
			params: {
				type: type,
				_page: page,
				_limit: limit,
				reviewed: reviewed
			}
		}
		)
	}

	static async reviewPostReport(reportId: number,
		reviewed: boolean): Promise<AxiosResponse<ReportResponse>> {
		return await $api.patch(`/reports/posts/${reportId}`, { reviewed })
	}

	static async reviewCommentReport(reportId: number,
		reviewed: boolean): Promise<AxiosResponse<ReportResponse>> {
		return await $api.patch(`/reports/comments/${reportId}`, { reviewed })
	}

	static async sendCommentReport(commentId: number,
		report: ReportRequest): Promise<AxiosResponse<ReportResponse>> {
		return await $api.post(`/reports/comments/${commentId}`, report)
	}

	static async sendPostReport(postId: number, report: ReportRequest): Promise<AxiosResponse<ReportResponse>> {
		return await $api.post(`/reports/posts/${postId}`, report)
	}
}
