CREATE SCHEMA IF NOT EXISTS artfolio;

CREATE TYPE artfolio.role AS ENUM ('USER', 'ADMIN');
CREATE CAST (VARCHAR AS artfolio.role) WITH INOUT AS IMPLICIT;


CREATE TABLE artfolio._user
(
    uuid            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(150) UNIQUE NOT NULL,
    username        VARCHAR(150) UNIQUE NOT NULL,
    password        VARCHAR             NOT NULL,
    secret_word     VARCHAR             NOT NULL,
    full_name       VARCHAR(40)         NOT NULL,
    country         VARCHAR(40),
    city            VARCHAR(40),
    additional_info VARCHAR(401),
    avatar_name     VARCHAR(150)        NOT NULL,
    avatar_type     VARCHAR(150)        NOT NULL,
    role            artfolio.role       NOT NULL,
    create_time     TIMESTAMP           NOT NULL,
    update_time     TIMESTAMP           NOT NULL
);

CREATE TABLE artfolio.post
(
    id                 BIGSERIAL PRIMARY KEY,
    owner_uuid         uuid         NOT NULL REFERENCES artfolio._user (uuid),
    name               VARCHAR(50)  NOT NULL,
    description        VARCHAR(401) NOT NULL,
    preview_media_name VARCHAR      NOT NULL,
    preview_type       VARCHAR(30)  NOT NULL,
    create_time        TIMESTAMP    NOT NULL
);

CREATE TABLE artfolio.media_file
(
    id        BIGSERIAL PRIMARY KEY,
    post_id   BIGINT      NOT NULL REFERENCES artfolio.post (id),
    type      VARCHAR(30) NOT NULL,
    file_name VARCHAR     NOT NULL,
    position  INT         NOT NULL
);

CREATE TABLE artfolio._like
(
    id        BIGSERIAL PRIMARY KEY,
    post_id   BIGINT NOT NULL REFERENCES artfolio.post (id),
    user_uuid uuid   NOT NULL REFERENCES artfolio._user (uuid)
);

CREATE TABLE artfolio.follower
(
    id            BIGSERIAL PRIMARY KEY,
    subscriber_id uuid NOT NULL REFERENCES artfolio._user (uuid),
    followed_id   uuid NOT NULL REFERENCES artfolio._user (uuid)
);

CREATE TABLE artfolio.comment
(
    id          BIGSERIAL PRIMARY KEY,
    user_uuid   uuid         NOT NULL REFERENCES artfolio._user (uuid),
    post_id     BIGINT       NOT NULL REFERENCES artfolio.post (id),
    comment     VARCHAR(300) NOT NULL,
    create_time TIMESTAMP    NOT NULL
);

CREATE TABLE artfolio.comment_report
(
    id          BIGSERIAL PRIMARY KEY,
    sender_uuid uuid         NOT NULL REFERENCES artfolio._user (uuid),
    comment_id  BIGINT       NOT NULL REFERENCES artfolio.comment (id),
    reason      VARCHAR(300) NOT NULL,
    reviewed    BOOLEAN      NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP    NOT NULL
);

CREATE TABLE artfolio.post_report
(
    id          BIGSERIAL PRIMARY KEY,
    sender_uuid uuid         NOT NULL REFERENCES artfolio._user (uuid),
    post_id     BIGINT       NOT NULL REFERENCES artfolio.post (id),
    reason      VARCHAR(300) NOT NULL,
    reviewed    BOOLEAN      NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP    NOT NULL
);
