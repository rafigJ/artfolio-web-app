INSERT INTO artfolio._user(uuid, email, username, password, secret_word, full_name, country, city, additional_info,
                           avatar_name, avatar_type, role, create_time, update_time, deleted)
VALUES ('7c826e51-b416-475d-97b1-e01b2835db52', 'bolton@vesteros.com', 'boltonArt',
        '$2a$10$Z2kInA94pRomtwFtMlZGoeNVyi/mvo2OZu0MAzYMWD6eILKwRm9am',
        '$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.', 'Ramsey Bolton', 'Russia', 'Ufa',
        'Born on Vesteros', 'dummy_file', 'image/jpeg', 'USER',
        '2024-06-04 22:16:35.176926', '2024-06-04 22:16:35.176926', FALSE),
       ('7c826e51-b416-475d-97b1-e01b2835db53', 'boltonAdmin@vesteros.com', 'boltonAdmin',
        '$2a$10$Z2kInA94pRomtwFtMlZGoeNVyi/mvo2OZu0MAzYMWD6eILKwRm9am',
        '$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.', 'Ramsey Admin', 'Admin County', 'Admin',
        'Born on Vesteros', 'dummy_file', 'image/jpeg', 'ADMIN',
        '2024-06-04 22:16:35.176926', '2024-06-04 22:16:35.176926', FALSE),
       ('7c826e51-b416-475d-97b1-e01b2835db54', 'boltonDeleted@vesteros.com', 'boltonDeleted',
        '$2a$10$Z2kInA94pRomtwFtMlZGoeNVyi/mvo2OZu0MAzYMWD6eILKwRm9am',
        '$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.', 'Ramsey Deleted', 'Deleted', 'Deleted',
        'Born on Vesteros', 'dummy_file', 'image/jpeg', 'USER',
        '2024-06-04 22:16:35.176926', '2024-06-04 22:16:35.176926', TRUE);

-- login: bolton@vesteros.com | boltonAdmin@vesteros.com | boltonDeleted@vesteros.com
-- password: somePassword19
-- secretWord: winterIsComing

INSERT INTO artfolio.post(owner_uuid, name, description, preview_media_name, preview_type, create_time, deleted)
VALUES ('7c826e51-b416-475d-97b1-e01b2835db52', 'post1', 'description', 'dummy_file', 'image/jpeg',
        '2024-06-04 22:16:35.176926', FALSE),
       ('7c826e51-b416-475d-97b1-e01b2835db53', 'deleted_post', 'deletedDescription', 'dummy_file',
        'image/jpeg',
        '2024-06-04 22:16:35.176926', TRUE);

INSERT INTO artfolio.media_file(post_id, type, file_name, position)
VALUES (1, 'image/jpeg', 'dummy_file', 0),
       (1, 'image/jpeg', 'dummy_file', 1),
       (2, 'image/jpeg', 'dummy_file', 0);


-- add 'dummy-like-subscribe; users
INSERT INTO artfolio._user(uuid, email, username, password, secret_word, full_name, country, city, additional_info,
                           avatar_name, avatar_type, role, create_time, update_time, deleted)
VALUES ('3c826e51-b416-475d-97b1-e01b2835db51', 'like1@vesteros.com', 'like1',
        '$2a$10$Z2kInA94pRomtwFtMlZGoeNVyi/mvo2OZu0MAzYMWD6eILKwRm9am',
        '$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.', 'Like 1', 'Liking1', 'Likes1',
        'Born on Vesteros', 'dummy_file', 'image/jpeg', 'USER',
        '2024-06-04 22:16:35.176926', '2024-06-04 22:16:35.176926', FALSE),
       ('3c826e51-b416-475d-97b1-e01b2835db52', 'like2@vesteros.com', 'like2',
        '$2a$10$Z2kInA94pRomtwFtMlZGoeNVyi/mvo2OZu0MAzYMWD6eILKwRm9am',
        '$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.', 'Like 2', 'Liking2', 'Likes2',
        'Born on Vesteros', 'dummy_file', 'image/jpeg', 'USER',
        '2024-06-04 22:16:35.176926', '2024-06-04 22:16:35.176926', FALSE);

INSERT INTO artfolio._like(post_id, user_uuid)
VALUES (1, '3c826e51-b416-475d-97b1-e01b2835db51'),
       (1, '3c826e51-b416-475d-97b1-e01b2835db52')
