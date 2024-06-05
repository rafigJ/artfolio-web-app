INSERT INTO artfolio._user(uuid, email, username, password, secret_word, full_name, country, city, additional_info,
                           avatar_name, avatar_type, role, create_time, update_time)
VALUES ('7c826e51-b416-475d-97b1-e01b2835db52', 'bolton@vesteros.com', 'boltonArt',
        '$2a$10$Ks0w1CzDYTwxYRrkQLBfe.wOmenhtnGBwwQbywSj16R9SQnsAzsFq',
        '$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.', 'Рамси Болтон', 'Россия', 'Уфа',
        'Родился я бастардом на Севере Вестероса.', '048f0ef6-4a37-4873-bede-c8038ae255d5', 'image/jpeg', 'USER',
        '2024-06-04 22:16:35.176926', '2024-06-04 22:16:35.176926');

INSERT INTO artfolio.post(owner_uuid, name, description, preview_media_name, preview_type, create_time, deleted)
VALUES ('7c826e51-b416-475d-97b1-e01b2835db52', 'post1', 'description', 'preview_media_name', 'image/jpeg',
        '2024-06-04 22:16:35.176926', false);

INSERT INTO artfolio.media_file(post_id, type, file_name, position)
VALUES (1, 'image/jpeg', 'dummy_file', 0),
       (1, 'image/jpeg', 'dummy_file', 1);
