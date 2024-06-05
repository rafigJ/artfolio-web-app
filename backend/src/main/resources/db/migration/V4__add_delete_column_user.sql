ALTER TABLE artfolio._user
    ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;

CREATE INDEX idx_user_deleted ON artfolio._user (deleted);
