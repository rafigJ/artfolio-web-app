-- update tables
DELETE FROM artfolio.media_file;
DELETE FROM artfolio._like;
DELETE FROM artfolio.post;
DELETE FROM artfolio._user;

SELECT setval('artfolio.media_file_id_seq', 1, false);
SELECT setval('artfolio.post_id_seq', 1, false);
SELECT setval('artfolio._like_id_seq', 1, false);

SELECT setval('artfolio.media_file_id_seq', (SELECT MAX(id) FROM artfolio.media_file), true);
SELECT setval('artfolio.post_id_seq', (SELECT MAX(id) FROM artfolio.post), true);
SELECT setval('artfolio._like_id_seq', (SELECT MAX(id) FROM artfolio._like), true);
