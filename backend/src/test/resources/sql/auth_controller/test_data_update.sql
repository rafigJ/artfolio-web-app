-- update user data
UPDATE artfolio._user
SET email           = 'bolton@vesteros.com',
    username        = 'boltonArt',
    password        = '$2a$10$Z2kInA94pRomtwFtMlZGoeNVyi/mvo2OZu0MAzYMWD6eILKwRm9am',
    secret_word     = '$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.',
    full_name       = 'Рамси Болтон',
    country         = 'Россия',
    city            = 'Уфа',
    additional_info = 'Родился я бастардом на Севере Вестероса.',
    avatar_name     = '048f0ef6-4a37-4873-bede-c8038ae255d5',
    avatar_type     = 'image/jpeg',
    role            = 'USER',
    create_time     = '2024-06-04 22:16:35.176926',
    update_time     = '2024-06-04 22:16:35.176926'
WHERE uuid = '7c826e51-b416-475d-97b1-e01b2835db52';
