-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: ics_db_project
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
-- Table structure for table `donates_in`
--

DROP TABLE IF EXISTS `donates_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donates_in` (
  `Drive_id` int NOT NULL,
  `Blood_id` int NOT NULL,
  `Dssn` int NOT NULL,
  `Date_of_Donation` date NOT NULL,
  `Status_of_Donation` varchar(45) NOT NULL,
  PRIMARY KEY (`Drive_id`,`Blood_id`,`Dssn`),
  KEY `SSN_idx` (`Dssn`),
  KEY `BLOODID_idx` (`Blood_id`),
  CONSTRAINT `BLOODID` FOREIGN KEY (`Blood_id`) REFERENCES `blood_product` (`Blood_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DRIVEID` FOREIGN KEY (`Drive_id`) REFERENCES `blood_drive` (`Drive_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Dssn` FOREIGN KEY (`Dssn`) REFERENCES `donor` (`Pssn`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donates_in`
--

LOCK TABLES `donates_in` WRITE;
/*!40000 ALTER TABLE `donates_in` DISABLE KEYS */;
/*!40000 ALTER TABLE `donates_in` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-15 13:57:00
