CREATE DATABASE  IF NOT EXISTS `supermed` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `supermed`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win32 (AMD64)
--
-- Host: localhost    Database: supermed
-- ------------------------------------------------------
-- Server version	5.7.14-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` char(40) NOT NULL UNIQUE,
  `password` char(40) NOT NULL,
  `first_name` char(40) NOT NULL,
  `middle_name` char(40) NOT NULL,
  `last_name` char(40) NOT NULL,
  `birth_date` char(40) NOT NULL,
  `address` char(40) NOT NULL,
  `contact_phone` char(40) NOT NULL,
  `role` char(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (0,'petya@yandex.ru','petya','petr','petrovich','ivanov','01.01.2001','Pushkina street, Kookushkina house','88005553535','patient');
INSERT INTO `users` VALUES (NULL,'vanya@yandex.ru','vanya','vanya','ivanovich','ivanov','01.01.2001','Pushkina street, Kookushkina house','88005553535','patient');
INSERT INTO `users` VALUES (NULL,'dima@yandex.ru','dima','dima','dmitrievich','dmitriev','01.01.2001','Pushkina street, Kookushkina house','88005553535','patient');
INSERT INTO `users` VALUES (NULL,'vasya@yandex.ru','vasya','vasya','vasilievich','vasiliev','01.01.2001','Pushkina street, Kookushkina house','88005553535','patient');
INSERT INTO `users` VALUES (NULL,'surgeon@yandex.ru','surgeon','Сергей','Сергеевич','Сергеев','01.01.2001','Pushkina street, Kookushkina house','88005553535','doctor');
INSERT INTO `users` VALUES (NULL,'manager@yandex.ru','manager','Михаил','Михаилович','Михайлов','01.01.2001','Pushkina street, Kookushkina house','88005553535','manager');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DROP TABLE IF EXISTS `positions`;
CREATE TABLE `positions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(40) NOT NULL UNIQUE,
  `is_medical` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `positions` WRITE;
INSERT INTO `positions` VALUES (0,'Терапевт',1);
INSERT INTO `positions` VALUES (NULL,'Хирург',1);
INSERT INTO `positions` VALUES (NULL,'Окулист',1);
INSERT INTO `positions` VALUES (NULL,'Стоматолог',1);
INSERT INTO `positions` VALUES (NULL,'Менеджер',0);
UNLOCK TABLES;


DROP TABLE IF EXISTS `branches`;
CREATE TABLE `branches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` char(40) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
LOCK TABLES `branches` WRITE;
INSERT INTO `branches` VALUES (0,'ул. Кукушкина, дом Колотушкина');
INSERT INTO `branches` VALUES (NULL,'ул. Пушкина, дом Пострелушкина');
INSERT INTO `branches` VALUES (NULL,'ул. Ватрушкина, дом Толстушкина');
UNLOCK TABLES;



DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees` (
  `id` int(11),
  `position_id` int(11),
  `branch_id` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
LOCK TABLES `employees` WRITE;
INSERT INTO `employees` VALUES (4,1,0);
INSERT INTO `employees` VALUES (5,4,0);
UNLOCK TABLES;
