export interface ReportResponce {
  id: number;
  postId: number;
  commentId?: number;
  comment?: string;
  reason: string;
  reviewed: boolean;
  targetUser: User;
  sendler: User;
}

interface User {
  fullName: string;
  username: string;
}