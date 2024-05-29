-- V2__add_indexes.sql

-- Adding indexes for _user table
CREATE INDEX idx_user_email ON artfolio._user(email);
CREATE INDEX idx_user_username ON artfolio._user(username);

-- Adding indexes for post table
CREATE INDEX idx_post_owner_uuid ON artfolio.post(owner_uuid);
CREATE INDEX idx_post_name ON artfolio.post(name);
CREATE INDEX idx_post_create_time ON artfolio.post(create_time);

-- Adding indexes for media_file table
CREATE INDEX idx_media_file_post_id ON artfolio.media_file(post_id);
CREATE INDEX idx_media_file_position ON artfolio.media_file(position);

-- Adding indexes for _like table
CREATE INDEX idx_like_post_id ON artfolio._like(post_id);
CREATE INDEX idx_like_user_uuid ON artfolio._like(user_uuid);

-- Adding indexes for follower table
CREATE INDEX idx_follower_subscriber_id ON artfolio.follower(subscriber_id);
CREATE INDEX idx_follower_followed_id ON artfolio.follower(followed_id);

-- Adding indexes for comment table
CREATE INDEX idx_comment_user_uuid ON artfolio.comment(user_uuid);
CREATE INDEX idx_comment_post_id ON artfolio.comment(post_id);
CREATE INDEX idx_comment_create_time ON artfolio.comment(create_time);

-- Adding indexes for comment_report table
CREATE INDEX idx_comment_report_sender_uuid ON artfolio.comment_report(sender_uuid);
CREATE INDEX idx_comment_report_comment_id ON artfolio.comment_report(comment_id);
CREATE INDEX idx_comment_report_reviewed ON artfolio.comment_report(reviewed);
CREATE INDEX idx_comment_report_create_time ON artfolio.comment_report(create_time);

-- Adding indexes for post_report table
CREATE INDEX idx_post_report_sender_uuid ON artfolio.post_report(sender_uuid);
CREATE INDEX idx_post_report_post_id ON artfolio.post_report(post_id);
CREATE INDEX idx_post_report_reviewed ON artfolio.post_report(reviewed);
CREATE INDEX idx_post_report_create_time ON artfolio.post_report(create_time);
