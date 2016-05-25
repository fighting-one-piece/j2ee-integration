-- ----------------------------  
-- Table structure for `T_PLAT_USER`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_USER`;  
CREATE TABLE `T_PLAT_USER` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `NAME` varchar(255) DEFAULT NULL,  
  `PASSWORD` varchar(255) DEFAULT NULL,  
  `IDENTITY` varchar(255) DEFAULT NULL,  
  `NICK_NAME` varchar(255) DEFAULT NULL, 
  `EMAIL` varchar(255) DEFAULT NULL, 
  `CREATE_TIME` datetime DEFAULT NULL,  
  `EXPIRE_TIME` datetime DEFAULT NULL,  
  `AVAILAN` varchar(255) DEFAULT NULL, 
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk; 

-- ----------------------------  
-- Table structure for `T_PLAT_ROLE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_ROLE`;  
CREATE TABLE `T_PLAT_ROLE` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `NAME` varchar(255) DEFAULT NULL,  
  `IDENTITY` varchar(255) DEFAULT NULL,  
  `DESCRIPTION` varchar(255) DEFAULT NULL,  
  `AVAILAN` varchar(255) DEFAULT NULL,     
  PRIMARY KEY (`ID`)                 
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------  
-- Table structure for `T_PLAT_GROUP`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_GROUP`;  
CREATE TABLE `T_PLAT_GROUP` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `NAME` varchar(255) DEFAULT NULL,  
  `IDENTITY` varchar(255) DEFAULT NULL,  
  `DESCRIPTION` varchar(255) DEFAULT NULL,  
  `AVAILAN` varchar(255) DEFAULT NULL,     
  PRIMARY KEY (`ID`)                 
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------  
-- Table structure for `T_PLAT_RESOURCE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_RESOURCE`;  
CREATE TABLE `T_PLAT_RESOURCE` (  
  `ID` bigint NOT NULL AUTO_INCREMENT,  
  `NAME` varchar(255) DEFAULT NULL,                                                            
  `TYPE` int(11) DEFAULT NULL,    
  `IDENTITY` varchar(255) DEFAULT NULL,                                                          
  `URL` varchar(255) DEFAULT NULL,                                                             
  `ICON` varchar(100) DEFAULT NULL,   
  `PRIORITY` varchar(100) DEFAULT NULL,                                                           
  `AVAILAN` varchar(255) DEFAULT NULL,                                                             
  `PARENT_ID` varchar(255) DEFAULT NULL,  
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------  
-- Table structure for `T_PLAT_PERMISSION`  
-- ---------------------------- 
DROP TABLE IF EXISTS `T_PLAT_PERMISSION`;
CREATE TABLE `T_PLAT_PERMISSION` (             
  `ID` bigint NOT NULL AUTO_INCREMENT,     
  `AUTH_STATUS` int(11) DEFAULT NULL,        
  `EXTEND_STATUS` int(11) DEFAULT NULL,      
  `PRINCIPAL_ID` bigint DEFAULT NULL,  
  `PRINCIPAL_TYPE` int(11) DEFAULT NULL,     
  `RESOURCE_ID` bigint DEFAULT NULL,   
  PRIMARY KEY (`ID`)               
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------  
-- Table structure for `T_PLAT_USER_ROLE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_USER_ROLE`;  
CREATE TABLE `T_PLAT_USER_ROLE` ( 
  `ID` bigint,
  `USER_ID` bigint NOT NULL,  
  `ROLE_ID` bigint NOT NULL,  
  `PRIORITY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`,`ROLE_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES T_PLAT_USER(`ID`),
  FOREIGN KEY (`ROLE_ID`) REFERENCES T_PLAT_ROLE(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------  
-- Table structure for `T_PLAT_USER_GROUP`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_USER_GROUP`;  
CREATE TABLE `T_PLAT_USER_GROUP` (  
  `ID` bigint,
  `USER_ID` bigint NOT NULL,  
  `GROUP_ID` bigint NOT NULL,
  `PRIORITY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`,`GROUP_ID`),
  FOREIGN KEY (`USER_ID`) REFERENCES T_PLAT_USER(`ID`),
  FOREIGN KEY (`GROUP_ID`) REFERENCES T_PLAT_GROUP(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------  
-- Table structure for `T_PLAT_GROUP_ROLE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_GROUP_ROLE`;  
CREATE TABLE `T_PLAT_GROUP_ROLE` (  
  `ID` bigint,
  `GROUP_ID` bigint NOT NULL,  
  `ROLE_ID` bigint NOT NULL,
  `PRIORITY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`,`ROLE_ID`),
  FOREIGN KEY (`GROUP_ID`) REFERENCES T_PLAT_GROUP(`ID`),
  FOREIGN KEY (`ROLE_ID`) REFERENCES T_PLAT_ROLE(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------  
-- Table structure for `T_PLAT_ROLE_RESOURCE`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_ROLE_RESOURCE`;  
CREATE TABLE `T_PLAT_ROLE_RESOURCE` (
  `ID` bigint,
  `ROLE_ID` bigint NOT NULL,  
  `RESOURCE_ID` bigint NOT NULL,  
  PRIMARY KEY (`ROLE_ID`,`RESOURCE_ID`),
  FOREIGN KEY (`ROLE_ID`) REFERENCES T_PLAT_ROLE(`ID`),
  FOREIGN KEY (`RESOURCE_ID`) REFERENCES T_PLAT_RESOURCES(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk; 

-- ----------------------------  
-- Table structure for `T_PLAT_CRAWL_DETAIL`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_CRAWL_DETAIL`;  
CREATE TABLE `T_PLAT_CRAWL_DETAIL` (
  `ID` bigint NOT NULL AUTO_INCREMENT, 
  `URL` varchar(255),
  `TYPE` varchar(255),
  `START_TIME` datetime,
  `END_TIME` datetime,
  `RULE` varchar(255),
  `STATUS` int(11),
  `LEVEL` int(11),
  `TOP_N` int(11),
  `PARENT_URL` varchar(255),
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk; 

-- ----------------------------  
-- Table structure for `T_PLAT_CRAWL_DETAIL_EXT`  
-- ----------------------------  
DROP TABLE IF EXISTS `T_PLAT_CRAWL_DETAIL_EXT`;  
CREATE TABLE `T_PLAT_CRAWL_DETAIL_EXT` (
  `ID` bigint NOT NULL AUTO_INCREMENT, 
  `DETAIL_KEY` varchar(255),
  `DETAIL_VALUE` varchar(255),
  `DETAIL_VALUE_EXT` text,
  `CRAWL_DETAIL_ID` bigint,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`CRAWL_DETAIL_ID`) REFERENCES T_PLAT_CRAWL_DETAIL(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk; 


