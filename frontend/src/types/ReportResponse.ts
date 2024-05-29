export interface ReportResponse {
  id: number;
  postId: number;
  commentId?: number;
  comment?: string;
  reason: string;
  reviewed: boolean;
  targetUser: User;
  sender: User;
  time: string;
}

interface User {
  fullName: string;
  username: string;
}