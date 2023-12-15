CREATE DATABASE  IF NOT EXISTS `ics_db_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ics_db_project`;
-- MySQL dump 10.13  Distrib 8.0.27, for macos11 (x86_64)
--
-- Host: 127.0.0.1    Database: ics_db_project
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blood_participant`
--

DROP TABLE IF EXISTS `blood_participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_participant` (
  `SSN` int NOT NULL,
  `Fname` varchar(30) NOT NULL,
  `Lname` varchar(30) NOT NULL,
  `Blood_type` varchar(3) NOT NULL,
  `Weight` int NOT NULL,
  `Height` int NOT NULL,
  `Gender` varchar(1) NOT NULL,
  `Birth_date` date NOT NULL,
  `Address` varchar(45) NOT NULL,
  `Phone` int NOT NULL,
  `Email` varchar(60) NOT NULL,
  PRIMARY KEY (`SSN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_participant`
--

LOCK TABLES `blood_participant` WRITE;
/*!40000 ALTER TABLE `blood_participant` DISABLE KEYS */;
INSERT INTO `blood_participant` VALUES (1001,'Nawaf','Almalki','O+',80,180,'M','2000-01-01','Riyadh',592786883,'nkmos9z@gmail.com'),(1002,'Ali','Samir','A-',66,176,'M','2007-03-13','Jeddah',589234178,'nkmalki77@gmail.com'),(1003,'Ahmed','Malik','B+',100,190,'M','1995-08-22','Khobar',567876522,'ahmedpopo0@gmail.com'),(1004,'Saad','Khalid','AB-',53,169,'M','2002-02-02','Dammam',546783674,'saad90903@gmail.com'),(1005,'Sarah','Saad','O-',70,176,'F','2000-02-20','Jeddah',538855446,'SarahJ909@gmail.com');
/*!40000 ALTER TABLE `blood_participant` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-15 18:34:53
