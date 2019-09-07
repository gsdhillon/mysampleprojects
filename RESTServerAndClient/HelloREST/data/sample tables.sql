DROP TABLE IF EXISTS `sampleweb`.`app_record`;
CREATE TABLE  `sampleweb`.`app_record` (
  `form_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `apply_date` datetime NOT NULL,
  `unit_code` int(10) unsigned NOT NULL,
  `emp_no` int(10) unsigned NOT NULL,
  `name` varchar(100) CHARACTER SET latin1 NOT NULL,
  `desig` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `data1` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `data2` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `status` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `remarks` varchar(500) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`form_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `sampleweb`.`app_visitors`;
CREATE TABLE  `sampleweb`.`app_visitors` (
  `form_id` int(10) unsigned NOT NULL,
  `s_no` int(10) unsigned NOT NULL,
  `name` varchar(100) NOT NULL,
  `dob` date DEFAULT NULL,
  `gender` varchar(10) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `photo` longblob,
  PRIMARY KEY (`form_id`,`s_no`),
  CONSTRAINT `FK_app_visitors_1` FOREIGN KEY (`form_id`) REFERENCES `app_record` (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;