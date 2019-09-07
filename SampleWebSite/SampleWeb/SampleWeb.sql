-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.22-rc-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema sampleweb
--

CREATE DATABASE IF NOT EXISTS sampleweb;
USE sampleweb;

--
-- Definition of table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `user_id` int(10) unsigned NOT NULL,
  `date_of_attendance` datetime NOT NULL,
  `in_time` datetime NOT NULL,
  `out_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`date_of_attendance`),
  CONSTRAINT `FK_attendance_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attendance`
--

/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` (`user_id`,`date_of_attendance`,`in_time`,`out_time`) VALUES 
 (1001,'2012-05-25 19:52:12','2012-05-25 19:52:12','2012-05-25 19:52:12'),
 (1002,'2012-05-25 19:52:29','2012-05-25 19:52:29','2012-05-25 19:52:29'),
 (1003,'2012-05-25 19:52:34','2012-05-25 19:52:34','2012-05-25 19:52:34');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;


--
-- Definition of table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(10) unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  `desig` varchar(45) NOT NULL,
  `division` varchar(45) NOT NULL,
  `date_of_birth` datetime NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`,`name`,`desig`,`division`,`date_of_birth`,`password`) VALUES 
 (1001,'Rakesh Kumar','Head','Computer','1956-03-08 00:00:00','1001'),
 (1002,'Ajeet Singh','Engineer','Computer','1972-03-08 00:00:00','1002'),
 (1003,'Rahul Sharma','Asst. Engineer','Computer','1978-04-12 00:00:00','1003');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
