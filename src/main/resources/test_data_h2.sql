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