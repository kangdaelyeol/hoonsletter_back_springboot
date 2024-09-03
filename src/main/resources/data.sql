INSERT INTO user_account (username, password, nickname, profile_url) values ('test1', '{noop}1234', 'testuser1', 'testurl'),
                                                                            ('test2', '{noop}1234', 'testuser2', 'testurl'),
                                                                            ('test3', '{noop}1234', 'testuser3', 'testurl'),
                                                                            ('test4', '{noop}1234', 'testuser4', 'testurl');

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

INSERT INTO user_authority (user_username, authority) values ('test1', 'ROLE_USER'),
                                                             ('test2', 'ROLE_USER'),
                                                             ('test3', 'ROLE_USER'),
                                                             ('test4', 'ROLE_USER');

INSERT INTO scene_message (id, part_order, content, size_type, color_type, scene_id) values (1, 1, 'p1', 'SMALL', 'WHITE', 1),
                                                                                            (4, 4, 'p4', 'LARGE', 'BLACK', 2),
                                                                                            (5, 5, 'p5', 'SMALL', 'WHITE', 2),
                                                                                            (10, 10, 'p10', 'SMALL', 'WHITE', 3),
                                                                                            (2, 2, 'p2', 'LARGE', 'BLACK', 1),
                                                                                            (3, 3, 'p3', 'SMALL', 'WHITE', 1),
                                                                                            (9, 9, 'p9', 'MEDIUM', 'WHITE', 3),
                                                                                            (6, 6, 'p6', 'MEDIUM', 'BLACK', 2),
                                                                                            (7, 7, 'p7', 'SMALL', 'BLACK', 2),
                                                                                            (8, 8, 'p8', 'MEDIUM', 'WHITE', 3);

INSERT INTO scene_picture (id, part_order, url, scene_id) values (3, 3, 'pic3', 2),
                                                                 (4, 4, 'pic4', 2),
                                                                 (5, 5, 'pic5', 2),
                                                                 (1, 1, 'pic1', 1),
                                                                 (2, 2, 'pic2', 1),
                                                                 (6, 6, 'pic6', 3),
                                                                 (7, 7, 'pic7', 3),
                                                                 (8, 8, 'pic8', 3);