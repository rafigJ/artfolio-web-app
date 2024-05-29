-- V3__add_deleted_column_and_indexes.sql

-- Adding 'deleted' column to artfolio.post table
ALTER TABLE artfolio.post
    ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;

-- Adding index for 'deleted' column in artfolio.post table
CREATE INDEX idx_post_deleted ON artfolio.post (deleted);

-- Adding 'deleted' column to artfolio.comment table
ALTER TABLE artfolio.comment
    ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;

-- Adding index for 'deleted' column in artfolio.comment table
CREATE INDEX idx_comment_deleted ON artfolio.comment (deleted);
