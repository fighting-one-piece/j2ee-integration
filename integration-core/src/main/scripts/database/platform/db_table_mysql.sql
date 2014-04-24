-- ----------------------------  
-- Table structure for `T_FETION_USER`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_USER`;  
CREATE TABLE `T_FETION_USER` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `ACCOUNT` varchar(50) DEFAULT NULL,  
  `ENABLE` int(11) DEFAULT NULL,  
  `PASSWORD` varchar(50) DEFAULT NULL,  
  `NICKNAME` varchar(255) DEFAULT NULL,  
  `ORG_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`ORG_ID`) REFERENCES ORGANIZATION(`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;  

-- ----------------------------  
-- Table structure for `T_FETION_ROLE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_ROLE`;  
CREATE TABLE `T_FETION_ROLE` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `CODE` varchar(255) NOT NULL,  
  `NAME` varchar(255) DEFAULT NULL,  
  `ENABLE` int(11) DEFAULT NULL,  
  `DESCRIPTION` varchar(500) DEFAULT NULL,  
  PRIMARY KEY (`id`)  
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8; 

-- ----------------------------  
-- Table structure for `T_FETION_RESOURCES`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_RESOURCES`;  
CREATE TABLE `T_FETION_RESOURCES` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `MEMO` longtext,  
  `NAME` varchar(50) DEFAULT NULL,  
  `PRIORITY` int(11) DEFAULT NULL,  
  `TYPE` varchar(11) DEFAULT NULL,  
  `URL` longtext,  
  `PARENT_ID` bigint DEFAULT NULL,
  `ENABLE` int(11) DEFAULT NULL,  
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`PARENT_ID`) REFERENCES T_FETION_RESOURCES(`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;  

-- ----------------------------  
-- Table structure for `T_FETION_GROUPS`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_GROUPS`;  
CREATE TABLE `T_FETION_GROUPS` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `NAME` varchar(255) NOT NULL,
  `ENABLE` int(11) DEFAULT NULL,  
  `DESCRIPTION` varchar(500) DEFAULT NULL,  
  PRIMARY KEY (`id`)  
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------  
-- Table structure for `T_FETION_USER_ROLE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_USER_ROLE`;  
CREATE TABLE `T_FETION_USER_ROLE` (  
  `UID` bigint NOT NULL,  
  `RID` bigint NOT NULL,  
  PRIMARY KEY (`UID`,`RID`),
  FOREIGN KEY (`UID`) REFERENCES T_FETION_USER(`ID`),
  FOREIGN KEY (`RID`) REFERENCES T_FETION_ROLE(`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8; 

-- ----------------------------  
-- Table structure for `T_FETION_ROLE_RESOURCES`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_ROLE_RESOURCES`;  
CREATE TABLE `T_FETION_ROLE_RESOURCES` (  
  `RID` bigint NOT NULL,  
  `RSID` bigint NOT NULL,  
  PRIMARY KEY (`RID`,`RSID`),
  FOREIGN KEY (`RID`) REFERENCES T_FETION_ROLE(`ID`),
  FOREIGN KEY (`RSID`) REFERENCES T_FETION_RESOURCES(`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;  

-- ----------------------------  
-- Table structure for `T_FETION_USER_GROUPS`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_USER_GROUPS`;  
CREATE TABLE `T_FETION_USER_GROUPS` (  
  `UID` bigint NOT NULL,  
  `GID` bigint NOT NULL,  
  PRIMARY KEY (`UID`,`GID`),
  FOREIGN KEY (`UID`) REFERENCES T_FETION_USER(`ID`),
  FOREIGN KEY (`GID`) REFERENCES T_FETION_GROUPS(`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;  

-- ----------------------------  
-- Table structure for `T_FETION_GROUPS_ROLE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_GROUPS_ROLE`;  
CREATE TABLE `T_FETION_GROUPS_ROLE` (  
  `GID` bigint NOT NULL,  
  `RID` bigint NOT NULL,  
  PRIMARY KEY (`GID`,`RID`),
  FOREIGN KEY (`GID`) REFERENCES T_FETION_GROUPS(`ID`),
  FOREIGN KEY (`RID`) REFERENCES T_FETION_ROLE(`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;  

-- ----------------------------  
-- Table structure for `T_FETION_ORGANIZATION`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_ORGANIZATION`;  
CREATE TABLE `T_FETION_ORGANIZATION` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `NAME` varchar(50) NOT NULL,
  `ENABLE` int(11) DEFAULT NULL,  
  `DESCRIPTION` varchar(500) DEFAULT NULL,  
  `PARENT_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`PARENT_ID`) REFERENCES T_FETION_ORGANIZATION(`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;  

-- ----------------------------  
-- Table structure for `T_FETION_COLUMNS`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_FETION_COLUMNS`;  
CREATE TABLE `T_FETION_COLUMNS` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `COLUMN_ID` varchar(255),  
  `COLUMN_SEQ` int,  
  `TABLE_NAME` varchar(200),  
  `HEADER_TEXT` varchar(200),  
  `DATA_FIELD` varchar(200),  
  `FORMAT_DATE` varchar(200),  
  `WIDTH` varchar(20),  
  `TEXT_ALIGN` varchar(20),  
  `VISIBLE` varchar(20),
  `ENABLE` int(11) DEFAULT NULL,  
  `PARENT_ID` bigint,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8; 


