export interface ReportRequest {
	reason: string
}

export enum ReportType {
	post = "POST",
	comment = "COMMENT"
}

export const ReportTypeValues = [ReportType.post, ReportType.comment]

export enum Reviewed {
	TRUE = "TRUE",
	FALSE = "FALSE"
}

export const ReviewedValues = [Reviewed.TRUE, Reviewed.FALSE]