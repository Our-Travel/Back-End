INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user1@example.com', 'user123', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user2@example.com', 'user234', '@y2password123', 0, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user3@example.com', 'user345', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user4@example.com', 'user346', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user5@example.com', 'user347', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user6@example.com', 'user348', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user7@example.com', 'user349', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user8@example.com', 'user342', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user9@example.com', 'user3421', '@y2password123', 1, 'OT');
INSERT INTO MEMBER (username, nick_name, password, host_authority, provider_type_code) VALUES ('user10@example.com', 'user3631', '@y2password123', 1, 'OT');

INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 1);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 3);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 4);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 5);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 6);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 7);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 8);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 9);
INSERT INTO HOST (introduction, region_code, member_id) VALUES ('안녕하세요', '11020', 10);

INSERT INTO KEYWORD (content) VALUES ('여행');

INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (1, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (2, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (3, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (4, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (5, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (6, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (7, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (8, 1);
INSERT INTO HASH_TAG(host_id, keyword_id) VALUES (9, 1);

INSERT INTO TRAVEL_BOARD(title, content, region_code, number_of_travelers, recruitment_period_start, recruitment_period_end,
                         journey_period_start, journey_period_end, recruitment_status, member_id) VALUES ('제목입니다.', '내용입니다.', '11020', 3, '2023-08-01', '2024-08-10', '2024-08-15', '2024-08-20', 'OPEN', 1);

INSERT INTO LIKE_BOARD(travel_board_id, member_id) VALUES (1, 2);

INSERT INTO TRAVEL_INFO(content_id, content_type_id, title, address, tel, tel_name, longitude, latitude, image, home_page, over_view) VALUES(1000, 15, '제목', '주소', '전화번호', '전화번호 출처', '100000.3', '100000.3', '이미지', '홈페이지', '내용');
INSERT INTO TRAVEL_INFO(content_id, content_type_id, title, address, tel, tel_name, longitude, latitude, image, home_page, over_view) VALUES(2000, 15, '제목', '주소', '전화번호', '전화번호 출처', '100000.2', '1000000.2', '이미지', '홈페이지', '내용');

INSERT INTO LIKED_TRAVEL_INFO(travel_info_id, member_id) VALUES (1, 2);