CREATE DATABASE  IF NOT EXISTS `cs261` /*!40100 DEFAULT CHARACTER SET utf8 */;

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
-- Table structure for table `sector`
--

DROP TABLE IF EXISTS `sector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sector` (
  `sector_id` int(11) NOT NULL AUTO_INCREMENT,
  `sector_name` varchar(255) NOT NULL,
  PRIMARY KEY (`sector_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `symbol`
--

DROP TABLE IF EXISTS `symbol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `symbol` (
  `symbol_id` int(11) NOT NULL AUTO_INCREMENT,
  `symbol_name` varchar(255) NOT NULL,
  `sector_id` int(11) NOT NULL,
  PRIMARY KEY (`symbol_id`),
  KEY `sector_id_idx` (`sector_id`),
  CONSTRAINT `sector_id` FOREIGN KEY (`sector_id`) REFERENCES `sector` (`sector_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `currency_id` int(11) NOT NULL AUTO_INCREMENT,
  `currency_name` varchar(45) NOT NULL,
  PRIMARY KEY (`currency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_name_UNIQUE` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade`
--

DROP TABLE IF EXISTS `trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade` (
  `trade_id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `buyer` varchar(255) NOT NULL,
  `seller` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `size` int(11) NOT NULL,
  `currency_id` int(11) NOT NULL,
  `symbol_id` int(11) NOT NULL,
  `bid_price` double NOT NULL,
  `ask_price` double NOT NULL,
  `pct_price_change` double NOT NULL,
  `category_id` int(11) NOT NULL,
  `is_anomalous` int(11) NOT NULL,
  PRIMARY KEY (`trade_id`),
  KEY `currency_id_idx` (`currency_id`),
  KEY `symbol_id_idx` (`symbol_id`),
  KEY `category_id_idx` (`category_id`),
  CONSTRAINT `category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `currency_id` FOREIGN KEY (`currency_id`) REFERENCES `currency` (`currency_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `symbol_id` FOREIGN KEY (`symbol_id`) REFERENCES `symbol` (`symbol_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sector`
--

LOCK TABLES `sector` WRITE;
/*!40000 ALTER TABLE `sector` DISABLE KEYS */;
INSERT INTO `sector` VALUES (1,'Financial'),(2,'Consumer Goods'),(3,'Services'),(4,'Basic Materials'),(5,'Industrial Goods'),(6,'Healthcare'),(7,'Other'),(8,'Utilities'),(9,'Technology');
/*!40000 ALTER TABLE `sector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `symbol`
--

LOCK TABLES `symbol` WRITE;
/*!40000 ALTER TABLE `symbol` DISABLE KEYS */;
INSERT INTO `symbol` VALUES (1,'DGE.L',2),(2,'BDEV.L',5),(3,'EZJ.L',3),(4,'BA.L',5),(5,'RRS.L',4),(6,'SGE.L',9),(7,'TW.L',5),(8,'IMB.L',2),(9,'TUI.L',7),(10,'VOD.L',9),(11,'SSE.L',8),(12,'INF.L',7),(13,'MDC.L',6),(14,'UU.L',8),(15,'FRES.L',4),(16,'SVT.L',8),(17,'RSA.L',1),(18,'MNDI.L',2),(19,'III.L',1),(20,'BP.L',4),(21,'BNZL.L',3),(22,'CNA.L',8),(23,'CCL.L',2),(24,'DC.L',2),(25,'BRBY.L',2),(26,'RB.L',2),(27,'ANTO.L',4),(28,'WPG.L',3),(29,'MERL.L',7),(30,'LGEN.L',1),(31,'ABF.L',2),(32,'BARC.L',1),(33,'DLG.L',1),(34,'OML.L',1),(35,'CRDA.L',5),(36,'PRU.L',1),(37,'SKG.L',5),(38,'BATS.L',2),(39,'WPP.L',3),(40,'HMSO.L',1),(41,'BAB.L',5),(42,'ADM.L',1),(43,'RDSA.L',4),(44,'GLEN.L',4),(45,'INTU.L',7),(46,'MCRO.L',9),(47,'SN.L',6),(48,'PPB.L',7),(49,'PSN.L',5),(50,'LLOY.L',1),(51,'SBRY.L',2),(52,'CCH.L',2),(53,'AZN.L',6),(54,'SHP.L',6),(55,'LAND.L',1),(56,'BLT.L',4),(57,'JMAT.L',4),(58,'CPI.L',3),(59,'SDR.L',1),(60,'STJ.L',1),(61,'EXPN.L',3),(62,'RDSB.L',4),(63,'RIO.L',4),(64,'IAG.L',3),(65,'HSBA.L',1),(66,'HL.L',1),(67,'PSON.L',3),(68,'WTB.L',3),(69,'IHG.L',3),(70,'CRH.L',5),(71,'PFG.L',1),(72,'STAN.L',1),(73,'AAL.L',4),(74,'TSCO.L',2),(75,'NXT.L',2),(76,'MKS.L',2),(77,'NG.L',8),(78,'GSK.L',6),(79,'BT.A.L',9),(80,'MRW.L',2),(81,'ITRK.L',3),(82,'AHT.L',3),(83,'RMG.L',3),(84,'AV.L',1),(85,'RBS.L',1),(86,'CTEC.L',6),(87,'DCC.L',1),(88,'ITV.L',3),(89,'WOS.L',5),(90,'GKN.L',2),(91,'HIK.L',6),(92,'RR.L',5),(93,'REL.L',3),(94,'LSE.L',1),(95,'BLND.L',1),(96,'SMIN.L',5),(97,'CPG.L',3),(98,'ULVR.L',2),(99,'KGF.L',3),(100,'SL.L',1),(101,'SKY.L',7);
/*!40000 ALTER TABLE `symbol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `currency`
--

LOCK TABLES `currency` WRITE;
/*!40000 ALTER TABLE `currency` DISABLE KEYS */;
INSERT INTO `currency` VALUES (1,'GBX');
/*!40000 ALTER TABLE `currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Default');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;