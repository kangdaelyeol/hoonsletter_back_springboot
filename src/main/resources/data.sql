INSERT INTO user_account (username, password, nickname, profile_url) values ('test1', '1234', 'testuser1', 'testurl'),
                                                                            ('test2', '1234', 'testuser2', 'testurl'),
                                                                            ('test3', '1234', 'testuser3', 'testurl'),
                                                                            ('test4', '1234', 'testuser4', 'testurl');

INSERT INTO letter (id, title, type, thumbnail_url, created_at, updatable, user_id) values (1, 'letter1', 'TYPE1', 'thumb1', '2023-10-01 11:10:29', true, 'test1'),
                                                                                           (2, 'letter2', 'TYPE2', 'thumb2', '2023-10-02 11:10:29', true, 'test2'),
                                                                                           (3, 'letter3', 'TYPE3', 'thumb3', '2023-10-03 11:10:29', true, 'test3'),
                                                                                           (4, 'letter4', 'TYPE4', 'thumb4', '2023-10-04 11:10:29', true, 'test4'),
                                                                                           (5, 'letter5', 'TYPE1', 'thumb5', '2023-10-05 11:10:29', true, 'test1');
INSERT INTO letter_scene (id, part_order, letter_id) values (1, 1, 1), (2, 2, 1), (3, 3, 1),
                                                            (4, 1, 2), (5, 2, 2), (6, 3, 2), (7, 4, 2),
                                                            (8, 1 ,3),
                                                            (9, 1, 4),
                                                            (10, 1, 5), (11, 2, 5);