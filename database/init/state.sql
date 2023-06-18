-- 테이블 ot__dev.state 구조 내보내기
CREATE TABLE IF NOT EXISTS `state` (
    `id` int(11) NOT NULL,
    `state_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 ot__dev.state:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` (`id`, `state_name`) VALUES
                                             (1, '서울특별시'),
                                             (2, '부산광역시'),
                                             (3, '대구광역시'),
                                             (4, '인천광역시'),
                                             (5, '광주광역시'),
                                             (6, '대전광역시'),
                                             (7, '울산광역시'),
                                             (8, '세종특별자치시'),
                                             (9, '경기도'),
                                             (10, '강원도'),
                                             (11, '충청북도'),
                                             (12, '충청남도'),
                                             (13, '전라북도'),
                                             (14, '전라남도'),
                                             (15, '경상북도'),
                                             (16, '경상남도'),
                                             (17, '제주특별자치도');
/*!40000 ALTER TABLE `state` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
