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
-- Records of T_FETION_USER  
-- ----------------------------  
INSERT INTO `T_FETION_USER` VALUES (1, 'admin', '1', 'ceb4f32325eda6142bd65215f4c0f371', '超级管理员', 1);  
INSERT INTO `T_FETION_USER` VALUES (2, 'user1', '1', 'f37a9fedea4408b316097a2017ab4c64', '用户1', 2);  
INSERT INTO `T_FETION_USER` VALUES (3, 'user2', '1', '2c458a07e6799b6a099b23e47fde06b8', '用户2', 3);  
INSERT INTO `T_FETION_USER` VALUES (4, 'user3', '1', '716a91a1b1a3fd5d00743798756caaa0', '用户3', 4); 

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
-- Records of T_FETION_ROLE  
-- ----------------------------  
INSERT INTO `T_FETION_ROLE` VALUES (1, 'ROLE_ADMIN', '超级管理员', '1', '超级管理员');  
INSERT INTO `T_FETION_ROLE` VALUES (2, 'ROLE_USER', '普通用户','1', '普通用户'); 

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
-- Records of T_FETION_RESOURCES  
-- ----------------------------  
INSERT INTO `T_FETION_RESOURCES` VALUES (1, null, 'main', '1', 'URL', '/bin-debug/fetionbi.html', null, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (2, null, 'login', '1', 'URL', '/login.jsp', null, 1);  
INSERT INTO `T_FETION_RESOURCES` VALUES (3, null, 'logout', '1', 'URL', '/logout.jsp', null, 1);  
INSERT INTO `T_FETION_RESOURCES` VALUES (4, null, 'timeout', '1', 'URL', '/timeout.jsp', null, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (5, null, 'root', '1', 'MENU', '', 1, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (6, null, '系统管理', '6', 'MENU', '', 5, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (7, null, '用户管理', '1', 'MENU', 'modules/authority/view/User.swf', 6, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (8, null, '角色管理', '1', 'MENU', 'modules/authority/view/Role.swf', 6, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (9, null, '用户组管理', '1', 'MENU', 'modules/authority/view/Groups.swf', 6, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (10, null, '资源管理', '1', 'MENU', 'modules/system/view/Resources.swf', 6, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (11, null, '机构管理', '1', 'MENU', 'modules/system/view/Organization.swf', 6, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (12, null, '标题管理', '1', 'MENU', 'modules/system/view/Columns.swf', 6, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (13, null, '手机飞信用户数报表', '1', 'MENU', '', 5, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (14, null, '日报', '1', 'MENU', '', 13, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (15, null, '手机飞信活跃日报表', '1', 'MENU', 'modules/report/view/mobileusernumber/DailyActive.swf', 14, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (16, null, '手机飞信登录日报表', '2', 'MENU', 'modules/report/view/mobileusernumber/DailyLogin.swf', 14, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (17, null, '手机飞信登录分布日报表', '3', 'MENU', 'modules/report/view/mobileusernumber/DailyLoginDistribute.swf', 14, 1); 
INSERT INTO `T_FETION_RESOURCES` VALUES (18, null, '手机飞信同时在线日报表', '4', 'MENU', 'modules/report/view/mobileusernumber/DailySTOnline.swf', 14, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (19, null, '飞信开通关闭日报表', '5', 'MENU', 'modules/report/view/usernumber/DailyOpenCloseUserNumber.swf', 14, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (20, null, '飞信CM在线用户日报表', '6', 'MENU', '', 14, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (21, null, '周报', '2', 'MENU', '', 13, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (22, null, '手机飞信登录周报表', '1', 'MENU', 'modules/report/view/mobileusernumber/WeeklyLogin.swf', 21, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (23, null, '月报', '3', 'MENU', '', 13, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (24, null, '手机飞信活跃月报表', '1', 'MENU', 'modules/report/view/mobileusernumber/MonthlyActive.swf', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (25, null, '手机飞信登录月报表', '2', 'MENU', 'modules/report/view/mobileusernumber/MonthlyLogin.swf', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (26, null, '飞信开通关闭月报表', '3', 'MENU', '', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (27, null, '新开通用户月报表', '4', 'MENU', '', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (28, null, '客户端新开通且活跃月报表', '5', 'MENU', '', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (29, null, '客户端新开通且客户端活跃月报表', '6', 'MENU', '', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (30, null, '新开通且客户端活跃月报表', '7', 'MENU', '', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (31, null, '飞信会员开通用户数月报表', '8', 'MENU', '', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (32, null, '飞信会员关闭用户数月报表', '9', 'MENU', '', 23, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (33, null, '质量报表', '2', 'MENU', '', 5, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (34, null, '日报', '1', 'MENU', '', 33, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (35, null, '手机飞信活跃质量好友日报', '1', 'MENU', '', 34, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (36, null, '手机飞信活跃质量消息日报', '2', 'MENU', '', 34, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (37, null, '手机飞信活跃质量时长日报', '3', 'MENU', '', 34, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (38, null, '手机飞信用户来源日报表', '4', 'MENU', '', 34, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (39, null, '手机飞信用户去向日报表', '5', 'MENU', '', 34, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (40, null, '全活跃用户日报表', '6', 'MENU', 'modules/report/view/usernumber/DailyAllActiveUserNumber.swf', 34, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (41, null, '全产品线活跃日报表', '7', 'MENU', 'modules/report/view/usernumber/DailyAllProductLineActive.swf', 34, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (42, null, '月报', '2', 'MENU', '', 33, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (43, null, '手机飞信好友数分布', '1', 'MENU', '', 42, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (44, null, '手机飞信活跃天数分布', '2', 'MENU', '', 42, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (45, null, '手机活跃质量分析月报表', '3', 'MENU', '', 42, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (46, null, '手机飞信用户来源月报表', '4', 'MENU', '', 42, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (47, null, '手机飞信用户去向月报表', '5', 'MENU', '', 42, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (48, null, '合作渠道报表', '3', 'MENU', '', 5, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (49, null, '日报', '1', 'MENU', '', 48, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (50, null, '渠道手机客户端日报表', '1', 'MENU', '', 49, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (51, null, '周报', '2', 'MENU', '', 48, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (52, null, '手机飞信客户端分渠道登录周统计', '1', 'MENU', '', 51, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (53, null, '月报', '3', 'MENU', '', 48, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (54, null, '渠道手机客户端月报表', '1', 'MENU', '', 53, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (55, null, '其他报表', '4', 'MENU', '', 5, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (56, null, '日报', '1', 'MENU', '', 55, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (57, null, '飞信手机活跃规模变化', '1', 'MENU', 'modules/report/view/other/DailyActiveScaleChange.swf', 56, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (58, null, '其他', '2', 'MENU', '', 55, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (59, null, '月同期手机飞信日活跃对比', '1', 'MENU', 'modules/report/view/other/MonthlyDayActiveCompare.swf', 58, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (60, null, '月同期手机飞信日累计活跃对比', '2', 'MENU', 'modules/report/view/other/MonthlyTotalDayActiveCompare.swf', 58, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (61, null, '首页地图', '3', 'MENU', '', 55, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (62, null, '首页地图信息', '1', 'MENU', '', 61, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (63, null, '飞信用户数分析报表', '5', 'MENU', '', 5, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (64, null, '日报', '1', 'MENU', '', 63, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (65, null, '飞信用户数日报表', '1', 'MENU', '', 64, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (66, null, '飞信活跃用户数日报总表', '2', 'MENU', '', 64, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (67, null, '飞信全网用户同时在线日报表', '3', 'MENU', '', 64, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (68, null, '常规产品同时在线用户峰值', '4', 'MENU', 'modules/report/view/usernumberanalyze/DailyCommonProductSTOnline.swf', 64, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (69, null, '异网用户注册及开通日报表', '5', 'MENU', '', 64, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (70, null, '月报', '2', 'MENU', '', 63, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (71, null, '飞信用户数月报表', '1', 'MENU', '', 70, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (72, null, '飞信活跃用户数月报总表', '2', 'MENU', '', 70, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (73, null, '飞信1及以下好友用户构成', '3', 'MENU', '', 70, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (74, null, '各活跃方式中1好友及其以下用户规模', '4', 'MENU', '', 70, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (75, null, '活跃用户业务量分布月报表', '5', 'MENU', '', 70, 1);
INSERT INTO `T_FETION_RESOURCES` VALUES (76, null, '会员好友数量分布分析月报', '6', 'MENU', '', 70, 1);


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
-- Records of T_FETION_GROUPS  
-- ----------------------------  
INSERT INTO `T_FETION_GROUPS` VALUES (1, 'GROUP_ADMIN', '1', '管理员组'); 
INSERT INTO `T_FETION_GROUPS` VALUES (2, 'GROUP_USER', '1', '普通用户组'); 

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
-- Records of T_FETION_USER_ROLE  
-- ----------------------------  
INSERT INTO `T_FETION_USER_ROLE` VALUES (1, 1);  
INSERT INTO `T_FETION_USER_ROLE` VALUES (2, 2);  
INSERT INTO `T_FETION_USER_ROLE` VALUES (3, 2);  
INSERT INTO `T_FETION_USER_ROLE` VALUES (4, 2); 

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
-- Records of T_FETION_ROLE_RESOURCES  
-- ----------------------------  
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 1);  
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 2);  
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 3);  
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 4);  
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 5);  
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 6);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 7);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 8);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 9);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 10);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 11);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 12);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 13);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 14);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 15);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 16);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 17);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 18);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 19);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 20);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 21);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 22);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 23);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 24);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 25);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 26);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 27);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 28);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 29);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 30);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 31);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 32);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 33);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 34);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 35);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 36);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 37);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 38);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 39);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 40);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 41);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 42);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 43);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 44);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 45);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 46);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 47);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 48);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 49);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 50);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 51);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 52);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 53);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 54);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 55);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 56);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 57);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 58);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 59);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 60);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 61);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 62);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 63);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 64);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 65);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 66);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 67);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 68);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 69);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 70);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 71);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 72);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 73);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 74);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 75);
INSERT INTO `T_FETION_ROLE_RESOURCES` VALUES (1, 76);

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
-- Records of T_FETION_USER_GROUPS  
-- ----------------------------  
INSERT INTO `T_FETION_USER_GROUPS` VALUES (1, 1);  
INSERT INTO `T_FETION_USER_GROUPS` VALUES (2, 2);  
INSERT INTO `T_FETION_USER_GROUPS` VALUES (3, 2);  
INSERT INTO `T_FETION_USER_GROUPS` VALUES (4, 2); 

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
-- Records of T_FETION_GROUPS_ROLE  
-- ----------------------------  
INSERT INTO `T_FETION_GROUPS_ROLE` VALUES (1, 1);  
INSERT INTO `T_FETION_GROUPS_ROLE` VALUES (2, 2);  

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
-- Records of T_FETION_ORGANIZATION  
-- ----------------------------  
INSERT INTO `T_FETION_ORGANIZATION` VALUES (1, '中软国际', 1, '中软国际', 0);  
INSERT INTO `T_FETION_ORGANIZATION` VALUES (2, '掌迅互动', 1,'掌迅互动', 1);  
INSERT INTO `T_FETION_ORGANIZATION` VALUES (3, 'org2', 1,'org2', 1);  
INSERT INTO `T_FETION_ORGANIZATION` VALUES (4, 'org3', 1,'org3', 1); 
INSERT INTO `T_FETION_ORGANIZATION` VALUES (5, '行业应用事业部', 1,'行业应用事业部', 2); 
INSERT INTO `T_FETION_ORGANIZATION` VALUES (6, 'org12', 1,'org12', 3); 


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

-- ----------------------------  
-- Records of T_FETION_COLUMNS  
-- ----------------------------  
INSERT INTO `T_FETION_COLUMNS` VALUES (1,'1',1,'authority.user','账号','account',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2,'2',2,'authority.user','密码','password',NULL,'200','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (3,'3',3,'authority.user','昵称','nickname',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (4,'4',4,'authority.user','状态','enableCN',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (5,'5',5,'authority.user','所属机构','organizationName',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (6,'1',1,'authority.role','ID','id',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (7,'2',2,'authority.role','代码','code',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (8,'3',3,'authority.role','名称','name',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (9,'4',4,'authority.role','状态','enableCN',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (10,'5',5,'authority.role','描述','description',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (11,'1',1,'authority.groups','ID','id',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (12,'2',2,'authority.groups','用户组名称','name',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (13,'3',3,'authority.groups','状态','enableCN',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (14,'4',4,'authority.groups','相关描述','description',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (15,'1',1,'system.organization','名称','@name',NULL,'180','left','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (16,'2',2,'system.organization','ID','@id',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (17,'3',3,'system.organization','状态','@enableCN',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (18,'4',4,'system.organization','描述','@description',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (19,'1',1,'system.resources','名称','@name',NULL,'300','left','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (20,'2',2,'system.resources','ID','@id',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (21,'3',3,'system.resources','优先权','@priority',NULL,'60','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (22,'4',4,'system.resources','URL','@url',NULL,'360','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (23,'5',5,'system.resources','状态','@enableCN',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (24,'6',6,'system.resources','备注','@memo',NULL,'100','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (25,'1',1,'system.columns','列顺序','columnSeq',NULL,'60','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (26,'2',2,'system.columns','表名','tableName',NULL,'270','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (27,'3',3,'system.columns','标题文本','headerText',NULL,'200','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (28,'4',4,'system.columns','数据字段','dataField',NULL,'150','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (29,'5',5,'system.columns','宽度','width',NULL,'60','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (30,'6',6,'system.columns','对齐方式','textAlign',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (31,'7',7,'system.columns','可见性','visible',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (32,'8',8,'system.columns','状态','enableCN',NULL,'80','center','true',1,0);

INSERT INTO `T_FETION_COLUMNS` VALUES (101,'1',1,'report.mobileusernumber.dailyactive','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (102,'2',2,'report.mobileusernumber.dailyactive','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (103,'3',3,'report.mobileusernumber.dailyactive','当日累计手机飞信活跃用户数','totalActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (104,'4',4,'report.mobileusernumber.dailyactive','当日手机飞信活跃用户数','activeUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (105,'5',5,'report.mobileusernumber.dailyactive','当日手机飞信活跃次数','activeNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (106,'6',6,'report.mobileusernumber.dailyactive','当日手机飞信消息条数','messageNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (107,'7',7,'report.mobileusernumber.dailyactive','当日手机飞信短信条数','smsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (108,'8',8,'report.mobileusernumber.dailyactive','当日手机飞信好友短信条数','friendSmsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (109,'9',9,'report.mobileusernumber.dailyactive','当日手机飞信手机好友短信条数','phoneFriendSmsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (110,'10',10,'report.mobileusernumber.dailyactive','人均消息量','averageMessageNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (111,'11',11,'report.mobileusernumber.dailyactive','人均短信量','averageSmsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (112,'2',2,'report.mobileusernumber.dailyactive','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (121,'1',1,'report.mobileusernumber.dailylogin','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (122,'2',2,'report.mobileusernumber.dailylogin','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (123,'3',3,'report.mobileusernumber.dailylogin','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (124,'4',4,'report.mobileusernumber.dailylogin','手机操作系统类型','osType',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (125,'5',5,'report.mobileusernumber.dailylogin','手机客户端版本','clientVersion',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (126,'6',6,'report.mobileusernumber.dailylogin','当日手机飞信登录用户数','loginUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (127,'7',7,'report.mobileusernumber.dailylogin','当日手机飞信登录次数','loginNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (151,'1',1,'report.mobileusernumber.dailylogindistribute','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (152,'2',2,'report.mobileusernumber.dailylogindistribute','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (153,'3',3,'report.mobileusernumber.dailylogindistribute','手机操作系统类型','osType',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (154,'4',4,'report.mobileusernumber.dailylogindistribute','手机客户端版本','clientVersion',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (155,'5',5,'report.mobileusernumber.dailylogindistribute','当日手机飞信累计登录用户数','totalLoginUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (156,'6',6,'report.mobileusernumber.dailylogindistribute','当日手机飞信登录用户数','loginUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (157,'7',7,'report.mobileusernumber.dailylogindistribute','当日手机飞信登录次数','loginNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (158,'8',8,'report.mobileusernumber.dailylogindistribute','当日手机飞信登录次数统计','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (159,'9',9,'report.mobileusernumber.dailylogindistribute','1次','loginNumber1',NULL,'80','center','true',1,158);
INSERT INTO `T_FETION_COLUMNS` VALUES (160,'10',10,'report.mobileusernumber.dailylogindistribute','2-5次','loginNumber2T5',NULL,'80','center','true',1,158);
INSERT INTO `T_FETION_COLUMNS` VALUES (161,'11',11,'report.mobileusernumber.dailylogindistribute','6-10次','loginNumber6T10',NULL,'80','center','true',1,158);
INSERT INTO `T_FETION_COLUMNS` VALUES (162,'12',12,'report.mobileusernumber.dailylogindistribute','11-20次','loginNumber11T20',NULL,'80','center','true',1,158);
INSERT INTO `T_FETION_COLUMNS` VALUES (163,'13',13,'report.mobileusernumber.dailylogindistribute','21-50次','loginNumber21T50',NULL,'80','center','true',1,158);
INSERT INTO `T_FETION_COLUMNS` VALUES (164,'14',14,'report.mobileusernumber.dailylogindistribute','51-100次','loginNumber51T100',NULL,'80','center','true',1,158);
INSERT INTO `T_FETION_COLUMNS` VALUES (165,'15',15,'report.mobileusernumber.dailylogindistribute','101次以上','loginNumber101',NULL,'80','center','true',1,158);
INSERT INTO `T_FETION_COLUMNS` VALUES (166,'2',2,'report.mobileusernumber.dailylogindistribute','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (131,'1',1,'report.mobileusernumber.dailystonline','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (132,'1',1,'report.mobileusernumber.dailystonline','SmartPhone','smartPhoneNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (133,'1',1,'report.mobileusernumber.dailystonline','Symbian','symbianNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (134,'1',1,'report.mobileusernumber.dailystonline','PocketPC','pocketPCNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (135,'1',1,'report.mobileusernumber.dailystonline','Java','javaNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (136,'1',1,'report.mobileusernumber.dailystonline','LinuxMoto','linuxMotoNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (137,'1',1,'report.mobileusernumber.dailystonline','Oms','omsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (138,'1',1,'report.mobileusernumber.dailystonline','BlackBerry','blackBerryNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (139,'1',1,'report.mobileusernumber.dailystonline','MTK客户端','mtkNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (140,'1',1,'report.mobileusernumber.dailystonline','iPhone','iPhoneNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (141,'1',1,'report.mobileusernumber.dailystonline','iPad','iPadNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (142,'1',1,'report.mobileusernumber.dailystonline','Android','androidNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (143,'1',1,'report.mobileusernumber.dailystonline','winPhoneNumber','winPhoneNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (144,'1',1,'report.mobileusernumber.dailystonline','iosPush','iosPushNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (145,'1',1,'report.mobileusernumber.dailystonline','WINPHONEPUSH','winPhonePushNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (146,'1',1,'report.mobileusernumber.dailystonline','合计','totalNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1550,'1',1,'report.usernumber.dailyopenandclose','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1551,'2',2,'report.usernumber.dailyopenandclose','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1552,'3',3,'report.usernumber.dailyopenandclose','当日累计注册用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1553,'4',4,'report.usernumber.dailyopenandclose','当日累计开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1554,'5',5,'report.usernumber.dailyopenandclose','当日开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1555,'6',6,'report.usernumber.dailyopenandclose','当日净增用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1556,'7',7,'report.usernumber.da	ilyopenandclose','当日注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1570,'1',1,'report.usernumber.dailycmonlineusernumber','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1571,'2',2,'report.usernumber.dailycmonlineusernumber','同时在线用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (171,'1',1,'report.mobileusernumber.weeklylogin','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (172,'2',2,'report.mobileusernumber.weeklylogin','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (173,'3',3,'report.mobileusernumber.weeklylogin','手机操作系统类型','osType',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (174,'4',4,'report.mobileusernumber.weeklylogin','手机客户端版本','clientVersion',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (175,'5',5,'report.mobileusernumber.weeklylogin','本周手机飞信登录用户数','loginUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (176,'6',6,'report.mobileusernumber.weeklylogin','本周手机飞信登录次数','loginNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (177,'2',2,'report.mobileusernumber.weeklylogin','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (181,'1',1,'report.mobileusernumber.monthlylogin','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (182,'2',2,'report.mobileusernumber.monthlylogin','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (183,'3',3,'report.mobileusernumber.monthlylogin','手机操作系统类型','osType',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (184,'4',4,'report.mobileusernumber.monthlylogin','手机客户端版本','clientVersion',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (185,'5',5,'report.mobileusernumber.monthlylogin','本月手机飞信登录用户数','loginUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (186,'6',6,'report.mobileusernumber.monthlylogin','本月手机飞信登录次数','loginNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (187,'2',2,'report.mobileusernumber.monthlylogin','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (191,'1',1,'report.mobileusernumber.monthlyactive','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (192,'2',2,'report.mobileusernumber.monthlyactive','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (193,'3',3,'report.mobileusernumber.monthlyactive','当月手机飞信活跃用户数','activeUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (194,'4',4,'report.mobileusernumber.monthlyactive','当月手机飞信活跃次数','activeNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (195,'5',5,'report.mobileusernumber.monthlyactive','当月手机飞信消息条数','messageNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (196,'6',6,'report.mobileusernumber.monthlyactive','当月手机飞信短信条数','smsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (197,'7',7,'report.mobileusernumber.monthlyactive','当月手机飞信好友短信条数','friendSmsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (198,'8',8,'report.mobileusernumber.monthlyactive','当月手机飞信手机好友短信条数','phoneFriendSmsNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (199,'2',2,'report.mobileusernumber.monthlyactive','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1700,'1',1,'report.usernumber.monthlyopencloseusernumber','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1701,'2',2,'report.usernumber.monthlyopencloseusernumber','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1702,'3',3,'report.usernumber.monthlyopencloseusernumber','当月累计注册用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1703,'4',4,'report.usernumber.monthlyopencloseusernumber','当月累计开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1704,'5',5,'report.usernumber.monthlyopencloseusernumber','当月开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1705,'6',6,'report.usernumber.monthlyopencloseusernumber','当月净增用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1706,'7',7,'report.usernumber.monthlyopencloseusernumber','当月注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1720,'1',1,'report.usernumber.monthlynewopenusernumber','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1721,'2',2,'report.usernumber.monthlynewopenusernumber','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1722,'3',3,'report.usernumber.monthlynewopenusernumber','当月PC开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1723,'4',4,'report.usernumber.monthlynewopenusernumber','当月手机开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1724,'5',5,'report.usernumber.monthlynewopenusernumber','当月短信开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1725,'6',6,'report.usernumber.monthlynewopenusernumber','当月WAP飞信开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1726,'7',7,'report.usernumber.monthlynewopenusernumber','当月BOSS开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1727,'8',8,'report.usernumber.monthlynewopenusernumber','当月客服开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1728,'9',9,'report.usernumber.monthlynewopenusernumber','当月营销开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1729,'10',10,'report.usernumber.monthlynewopenusernumber','当月批量开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1730,'11',11,'report.usernumber.monthlynewopenusernumber','当月其他方式开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1770,'1',1,'report.usernumber.monthlyclientnewopenactive','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1771,'2',2,'report.usernumber.monthlyclientnewopenactive','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1772,'3',3,'report.usernumber.monthlyclientnewopenactive','当月开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1773,'4',4,'report.usernumber.monthlyclientnewopenactive','当月客户端开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1774,'5',5,'report.usernumber.monthlyclientnewopenactive','当月PC客户端开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1775,'6',6,'report.usernumber.monthlyclientnewopenactive','当月手机客户端开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1776,'7',7,'report.usernumber.monthlyclientnewopenactive','当月短信开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1777,'8',8,'report.usernumber.monthlyclientnewopenactive','当月BOSS开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1778,'9',9,'report.usernumber.monthlyclientnewopenactive','当月批开开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1779,'10',10,'report.usernumber.monthlyclientnewopenactive','当月其它方式开通且活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1790,'1',1,'report.usernumber.monthlyclientnewandclientactive','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1791,'2',2,'report.usernumber.monthlyclientnewandclientactive','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1792,'3',3,'report.usernumber.monthlyclientnewandclientactive','当月客户端开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1793,'4',4,'report.usernumber.monthlyclientnewandclientactive','当月PC客户端开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1794,'5',5,'report.usernumber.monthlyclientnewandclientactive','当月手机客户端开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1795,'6',6,'report.usernumber.monthlyclientnewandclientactive','当月短信开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1796,'7',7,'report.usernumber.monthlyclientnewandclientactive','当月BOSS开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1797,'8',8,'report.usernumber.monthlyclientnewandclientactive','当月批开开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1798,'9',9,'report.usernumber.monthlyclientnewandclientactive','当月其它方式开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1810,'1',1,'report.usernumber.monthlynewopenclientactive','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1811,'2',2,'report.usernumber.monthlynewopenclientactive','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1812,'3',3,'report.usernumber.monthlynewopenclientactive','当月开通且客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1813,'4',4,'report.usernumber.monthlynewopenclientactive','当月开通且PC客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1814,'5',5,'report.usernumber.monthlynewopenclientactive','当月开通且短信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1815,'6',6,'report.usernumber.monthlynewopenclientactive','当月开通且IVR活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1140,'1',1,'report.memberanalyze.monthlymemberopenusernumber','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1141,'2',2,'report.memberanalyze.monthlymemberopenusernumber','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1142,'3',3,'report.memberanalyze.monthlymemberopenusernumber','当月飞信会员主动开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1143,'4',4,'report.memberanalyze.monthlymemberopenusernumber','客户端','',NULL,'80','center','true',1,1142);
INSERT INTO `T_FETION_COLUMNS` VALUES (1144,'5',5,'report.memberanalyze.monthlymemberopenusernumber','web','',NULL,'80','center','true',1,1142);
INSERT INTO `T_FETION_COLUMNS` VALUES (1145,'6',6,'report.memberanalyze.monthlymemberopenusernumber','短信','',NULL,'80','center','true',1,1142);
INSERT INTO `T_FETION_COLUMNS` VALUES (1146,'7',7,'report.memberanalyze.monthlymemberopenusernumber','wap','',NULL,'80','center','true',1,1142);
INSERT INTO `T_FETION_COLUMNS` VALUES (1147,'8',8,'report.memberanalyze.monthlymemberopenusernumber','主动开通','',NULL,'80','center','true',1,1142);
INSERT INTO `T_FETION_COLUMNS` VALUES (1148,'9',9,'report.memberanalyze.monthlymemberopenusernumber','当月飞信会员被动开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1149,'10',10,'report.memberanalyze.monthlymemberopenusernumber','boss','',NULL,'80','center','true',1,1148);
INSERT INTO `T_FETION_COLUMNS` VALUES (1150,'11',11,'report.memberanalyze.monthlymemberopenusernumber','客服','',NULL,'80','center','true',1,1148);
INSERT INTO `T_FETION_COLUMNS` VALUES (1151,'12',12,'report.memberanalyze.monthlymemberopenusernumber','批量','',NULL,'80','center','true',1,1148);
INSERT INTO `T_FETION_COLUMNS` VALUES (1152,'13',13,'report.memberanalyze.monthlymemberopenusernumber','被动开通','',NULL,'80','center','true',1,1148);
INSERT INTO `T_FETION_COLUMNS` VALUES (1153,'14',14,'report.memberanalyze.monthlymemberopenusernumber','当月飞信会员开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1160,'1',1,'report.memberanalyze.monthlymembercloseusernumber','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1161,'2',2,'report.memberanalyze.monthlymembercloseusernumber','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1162,'3',3,'report.memberanalyze.monthlymembercloseusernumber','当月飞信会员主动关闭用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1163,'4',4,'report.memberanalyze.monthlymembercloseusernumber','客户端','',NULL,'80','center','true',1,1162);
INSERT INTO `T_FETION_COLUMNS` VALUES (1164,'5',5,'report.memberanalyze.monthlymembercloseusernumber','web','',NULL,'80','center','true',1,1162);
INSERT INTO `T_FETION_COLUMNS` VALUES (1165,'6',6,'report.memberanalyze.monthlymembercloseusernumber','短信','',NULL,'80','center','true',1,1162);
INSERT INTO `T_FETION_COLUMNS` VALUES (1166,'7',7,'report.memberanalyze.monthlymembercloseusernumber','wap','',NULL,'80','center','true',1,1162);
INSERT INTO `T_FETION_COLUMNS` VALUES (1167,'8',8,'report.memberanalyze.monthlymembercloseusernumber','主动关闭','',NULL,'80','center','true',1,1162);
INSERT INTO `T_FETION_COLUMNS` VALUES (1168,'9',9,'report.memberanalyze.monthlymembercloseusernumber','当月飞信会员被动关闭用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1169,'10',10,'report.memberanalyze.monthlymembercloseusernumber','boss','',NULL,'80','center','true',1,1168);
INSERT INTO `T_FETION_COLUMNS` VALUES (1170,'11',11,'report.memberanalyze.monthlymembercloseusernumber','客服','',NULL,'80','center','true',1,1168);
INSERT INTO `T_FETION_COLUMNS` VALUES (1171,'12',12,'report.memberanalyze.monthlymembercloseusernumber','批量','',NULL,'80','center','true',1,1168);
INSERT INTO `T_FETION_COLUMNS` VALUES (1172,'13',13,'report.memberanalyze.monthlymembercloseusernouumber','被动关闭','',NULL,'80','center','true',1,1168);
INSERT INTO `T_FETION_COLUMNS` VALUES (1173,'14',14,'report.memberanalyze.monthlymembercloseusernumber','当月飞信会员关闭用户数','',NULL,'80','center','true',1,0);

INSERT INTO `T_FETION_COLUMNS` VALUES (2120,'1',1,'report.quality.dailyactivefriend','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2121,'2',2,'report.quality.dailyactivefriend','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2122,'3',3,'report.quality.dailyactivefriend','手机飞信操作系统','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2123,'4',4,'report.quality.dailyactivefriend','当日累计手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2124,'5',5,'report.quality.dailyactivefriend','当日手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2125,'6',6,'report.quality.dailyactivefriend','当日手机飞信活跃次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2126,'7',7,'report.quality.dailyactivefriend','当日手机飞信活跃用户好友数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2127,'8',8,'report.quality.dailyactivefriend','当日手机飞信活跃用户人均好友数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2128,'9',9,'report.quality.dailyactivefriend','当日手机飞信活跃用户好友分布','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2129,'10',10,'report.quality.dailyactivefriend','0-1好友','',NULL,'80','center','true',1,2128);
INSERT INTO `T_FETION_COLUMNS` VALUES (2130,'11',11,'report.quality.dailyactivefriend','2-5好友','',NULL,'80','center','true',1,2128);
INSERT INTO `T_FETION_COLUMNS` VALUES (2131,'12',12,'report.quality.dailyactivefriend','6-10好友','',NULL,'80','center','true',1,2128);
INSERT INTO `T_FETION_COLUMNS` VALUES (2132,'13',13,'report.quality.dailyactivefriend','11-20好友','',NULL,'80','center','true',1,2128);
INSERT INTO `T_FETION_COLUMNS` VALUES (2133,'14',14,'report.quality.dailyactivefriend','21-35好友','',NULL,'80','center','true',1,2128);
INSERT INTO `T_FETION_COLUMNS` VALUES (2134,'15',15,'report.quality.dailyactivefriend','36-70好友','',NULL,'80','center','true',1,2128);
INSERT INTO `T_FETION_COLUMNS` VALUES (2135,'16',16,'report.quality.dailyactivefriend','71好友以上','',NULL,'80','center','true',1,2128);
INSERT INTO `T_FETION_COLUMNS` VALUES (2136,'17',17,'report.quality.dailyactivefriend','当日手机飞信活跃用户联系好友数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2137,'18',18,'report.quality.dailyactivefriend','当日手机飞信活跃用户人均联系好友数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2138,'19',19,'report.quality.dailyactivefriend','当日手机飞信活跃用户联系好友分布','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2139,'20',20,'report.quality.dailyactivefriend','0-1好友','',NULL,'80','center','true',1,2138);
INSERT INTO `T_FETION_COLUMNS` VALUES (2140,'21',21,'report.quality.dailyactivefriend','2-5好友','',NULL,'80','center','true',1,2138);
INSERT INTO `T_FETION_COLUMNS` VALUES (2141,'22',22,'report.quality.dailyactivefriend','6-10好友','',NULL,'80','center','true',1,2138);
INSERT INTO `T_FETION_COLUMNS` VALUES (2142,'23',23,'report.quality.dailyactivefriend','11-20好友','',NULL,'80','center','true',1,2138);
INSERT INTO `T_FETION_COLUMNS` VALUES (2143,'24',24,'report.quality.dailyactivefriend','21-35好友','',NULL,'80','center','true',1,2138);
INSERT INTO `T_FETION_COLUMNS` VALUES (2144,'25',25,'report.quality.dailyactivefriend','36-70好友','',NULL,'80','center','true',1,2138);
INSERT INTO `T_FETION_COLUMNS` VALUES (2145,'26',26,'report.quality.dailyactivefriend','71好友以上','',NULL,'80','center','true',1,2138);
INSERT INTO `T_FETION_COLUMNS` VALUES (2146,'27',27,'report.quality.dailyactivefriend','当日手机飞信活跃用户添加好友次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2147,'28',28,'report.quality.dailyactivefriend','当日手机飞信活跃用户人均添加好友次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2148,'29',29,'report.quality.dailyactivefriend','当日手机飞信活跃用户添加好友次数分布','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2149,'30',30,'report.quality.dailyactivefriend','0次','',NULL,'80','center','true',1,2148);
INSERT INTO `T_FETION_COLUMNS` VALUES (2150,'31',31,'report.quality.dailyactivefriend','1次','',NULL,'80','center','true',1,2148);
INSERT INTO `T_FETION_COLUMNS` VALUES (2151,'32',32,'report.quality.dailyactivefriend','2-10次','',NULL,'80','center','true',1,2148);
INSERT INTO `T_FETION_COLUMNS` VALUES (2152,'33',33,'report.quality.dailyactivefriend','11-20次','',NULL,'80','center','true',1,2148);
INSERT INTO `T_FETION_COLUMNS` VALUES (2153,'34',34,'report.quality.dailyactivefriend','20-50次','',NULL,'80','center','true',1,2148);
INSERT INTO `T_FETION_COLUMNS` VALUES (2154,'35',35,'report.quality.dailyactivefriend','50次以上','',NULL,'80','center','true',1,2148);
INSERT INTO `T_FETION_COLUMNS` VALUES (2155,'36',36,'report.quality.dailyactivefriend','当日手机飞信活跃用户同意添加好友次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2156,'37',37,'report.quality.dailyactivefriend','当日手机飞信活跃用户人均同意添加好友次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2157,'38',38,'report.quality.dailyactivefriend','当日手机飞信活跃用户同意添加好友次数分布','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2158,'39',39,'report.quality.dailyactivefriend','0次','',NULL,'80','center','true',1,2157);
INSERT INTO `T_FETION_COLUMNS` VALUES (2159,'40',40,'report.quality.dailyactivefriend','1次','',NULL,'80','center','true',1,2157);
INSERT INTO `T_FETION_COLUMNS` VALUES (2160,'41',41,'report.quality.dailyactivefriend','2-10次','',NULL,'80','center','true',1,2157);
INSERT INTO `T_FETION_COLUMNS` VALUES (2161,'42',42,'report.quality.dailyactivefriend','11-20次','',NULL,'80','center','true',1,2157);
INSERT INTO `T_FETION_COLUMNS` VALUES (2162,'43',43,'report.quality.dailyactivefriend','20-50次','',NULL,'80','center','true',1,2157);
INSERT INTO `T_FETION_COLUMNS` VALUES (2163,'44',44,'report.quality.dailyactivefriend','50次以上','',NULL,'80','center','true',1,2157);
INSERT INTO `T_FETION_COLUMNS` VALUES (2164,'2',2,'report.quality.dailyactivefriend','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2170,'1',1,'report.quality.dailyactivemessage','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2171,'2',2,'report.quality.dailyactivemessage','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2172,'3',3,'report.quality.dailyactivemessage','手机飞信操作系统','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2173,'4',4,'report.quality.dailyactivemessage','当日累计手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2174,'5',5,'report.quality.dailyactivemessage','当日手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2175,'6',6,'report.quality.dailyactivemessage','当日手机飞信活跃次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2176,'7',7,'report.quality.dailyactivemessage','当日手机飞信活跃用户消息量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2177,'8',8,'report.quality.dailyactivemessage','当日手机飞信活跃用户人均消息量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2178,'9',9,'report.quality.dailyactivemessage','当日手机飞信活跃用户发送消息量分布','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2179,'10',10,'report.quality.dailyactivemessage','发送0条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2180,'11',11,'report.quality.dailyactivemessage','发送1条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2181,'12',12,'report.quality.dailyactivemessage','发送2－10条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2182,'13',13,'report.quality.dailyactivemessage','发送11－50条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2183,'14',14,'report.quality.dailyactivemessage','发送51－100条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2184,'15',15,'report.quality.dailyactivemessage','发送100条以上','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2185,'16',16,'report.quality.dailyactivemessage','当日手机飞信活跃用户短信量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2186,'17',17,'report.quality.dailyactivemessage','当日手机飞信活跃用户人均短信量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2187,'18',18,'report.quality.dailyactivemessage','当日手机飞信活跃用户活跃用户发送短信量分布','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2188,'19',19,'report.quality.dailyactivemessage','发送0条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2189,'20',20,'report.quality.dailyactivemessage','发送1条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2190,'21',21,'report.quality.dailyactivemessage','发送2－10条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2191,'22',22,'report.quality.dailyactivemessage','发送11－50条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2192,'23',23,'report.quality.dailyactivemessage','发送51－100条','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2193,'24',24,'report.quality.dailyactivemessage','发送100条以上','',NULL,'80','center','true',1,2178);
INSERT INTO `T_FETION_COLUMNS` VALUES (2194,'2',2,'report.quality.dailyactivemessage','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2201,'1',1,'report.quality.dailyactiveduration','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2202,'2',2,'report.quality.dailyactiveduration','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2203,'3',3,'report.quality.dailyactiveduration','手机飞信操作系统','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2204,'4',4,'report.quality.dailyactiveduration','当日累计手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2205,'5',5,'report.quality.dailyactiveduration','当日手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2206,'6',6,'report.quality.dailyactiveduration','当日手机飞信活跃次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2207,'7',7,'report.quality.dailyactiveduration','当日手机飞信活跃用户手机飞信在线时长(分钟)','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2208,'8',8,'report.quality.dailyactiveduration','当日手机飞信活跃用户人均手机飞信在线时长(分钟)','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2209,'9',9,'report.quality.dailyactiveduration','当日手机飞信活跃用户手机飞信在线时长分布','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2210,'10',10,'report.quality.dailyactiveduration','0-1分钟','',NULL,'80','center','true',1,02209);
INSERT INTO `T_FETION_COLUMNS` VALUES (2211,'11',11,'report.quality.dailyactiveduration','2-8分钟','',NULL,'80','center','true',1,2209);
INSERT INTO `T_FETION_COLUMNS` VALUES (2212,'12',12,'report.quality.dailyactiveduration','8分钟-1小时','',NULL,'80','center','true',1,2209);
INSERT INTO `T_FETION_COLUMNS` VALUES (2213,'13',13,'report.quality.dailyactiveduration','1-12小时','',NULL,'80','center','true',1,2209);
INSERT INTO `T_FETION_COLUMNS` VALUES (2214,'14',14,'report.quality.dailyactiveduration','12-24小时','',NULL,'80','center','true',1,2209);
INSERT INTO `T_FETION_COLUMNS` VALUES (2215,'15',15,'report.quality.dailyactiveduration','24-48小时','',NULL,'80','center','true',1,2209);
INSERT INTO `T_FETION_COLUMNS` VALUES (2216,'2',2,'report.quality.dailyactiveduration','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2230,'1',1,'report.quality.dailyusersource','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2231,'2',2,'report.quality.dailyusersource','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2232,'3',3,'report.quality.dailyusersource','客户端','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2233,'4',4,'report.quality.dailyusersource','当月手机飞信活跃用户在本月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2234,'5',5,'report.quality.dailyusersource','当日累计手机飞信活跃用户数','',NULL,'80','center','true',1,2233);
INSERT INTO `T_FETION_COLUMNS` VALUES (2235,'6',6,'report.quality.dailyusersource','累计新开通手机飞信活跃用户数','',NULL,'80','center','true',1,2233);
INSERT INTO `T_FETION_COLUMNS` VALUES (2236,'7',7,'report.quality.dailyusersource','累计手机飞信活跃且其他客户端活跃用户数','',NULL,'80','center','true',1,2233);
INSERT INTO `T_FETION_COLUMNS` VALUES (2237,'8',8,'report.quality.dailyusersource','当月手机飞信活跃用户在上月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2238,'9',9,'report.quality.dailyusersource','连续2个月手机飞信活跃用户数','',NULL,'80','center','true',1,2237);
INSERT INTO `T_FETION_COLUMNS` VALUES (2239,'10',10,'report.quality.dailyusersource','上月非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2237);
INSERT INTO `T_FETION_COLUMNS` VALUES (2240,'11',11,'report.quality.dailyusersource','上月飞信不活跃用户数','',NULL,'80','center','true',1,2237);
INSERT INTO `T_FETION_COLUMNS` VALUES (2241,'12',12,'report.quality.dailyusersource','当月手机飞信活跃用户在上上月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2242,'13',13,'report.quality.dailyusersource','连续3个月手机飞信活跃用户数','',NULL,'80','center','true',1,2241);
INSERT INTO `T_FETION_COLUMNS` VALUES (2243,'14',14,'report.quality.dailyusersource','上上月、当月手机飞信活跃用户数','',NULL,'80','center','true',1,2241);
INSERT INTO `T_FETION_COLUMNS` VALUES (2244,'15',15,'report.quality.dailyusersource','上上月非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2241);
INSERT INTO `T_FETION_COLUMNS` VALUES (2245,'16',16,'report.quality.dailyusersource','上上月飞信不活跃用户数','',NULL,'80','center','true',1,2241);
INSERT INTO `T_FETION_COLUMNS` VALUES (2260,'1',1,'report.quality.dailyuseraway','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2261,'2',2,'report.quality.dailyuseraway','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2262,'3',3,'report.quality.dailyuseraway','客户端','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2263,'4',4,'report.quality.dailyuseraway','上月稽核后手机飞信活跃用户在当月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2264,'5',5,'report.quality.dailyuseraway','上月当月日累计手机飞信活跃用户数','',NULL,'80','center','true',1,2263);
INSERT INTO `T_FETION_COLUMNS` VALUES (2265,'6',6,'report.quality.dailyuseraway','上月当月日累计非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2263);
INSERT INTO `T_FETION_COLUMNS` VALUES (2266,'7',7,'report.quality.dailyuseraway','上月手机飞信活跃用户当月日累计非手机飞信活跃PC活跃的用户数','',NULL,'80','center','true',1,2263);
INSERT INTO `T_FETION_COLUMNS` VALUES (2267,'8',8,'report.quality.dailyuseraway','上月手机飞信活跃用户当月日累计非手机飞信活跃短信活跃的用户数','',NULL,'80','center','true',1,2263);
INSERT INTO `T_FETION_COLUMNS` VALUES (2268,'9',9,'report.quality.dailyuseraway','上月手机飞信活跃用户当月日累计非手机飞信活跃WAP活跃的用户数','',NULL,'80','center','true',1,2263);
INSERT INTO `T_FETION_COLUMNS` VALUES (2269,'10',10,'report.quality.dailyuseraway','上月当月日累计飞信不活跃用户数','',NULL,'80','center','true',1,2263);
INSERT INTO `T_FETION_COLUMNS` VALUES (2270,'11',11,'report.quality.dailyuseraway','上月当月日累计注销飞信用户数','',NULL,'80','center','true',1,2263);
INSERT INTO `T_FETION_COLUMNS` VALUES (2271,'12',12,'report.quality.dailyuseraway','上上月稽核后手机飞信活跃用户在当月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2272,'13',13,'report.quality.dailyuseraway','上上月当月日累计连续3个月手机飞信活跃的用户数','',NULL,'80','center','true',1,2271);
INSERT INTO `T_FETION_COLUMNS` VALUES (2273,'14',14,'report.quality.dailyuseraway','上上月当月日累计手机飞信活跃用户数','',NULL,'80','center','true',1,2271);
INSERT INTO `T_FETION_COLUMNS` VALUES (2274,'15',15,'report.quality.dailyuseraway','上上月当月日累计非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2271);
INSERT INTO `T_FETION_COLUMNS` VALUES (2275,'16',16,'report.quality.dailyuseraway','上上月当月日累计飞信不活跃用户数','',NULL,'80','center','true',1,2271);
INSERT INTO `T_FETION_COLUMNS` VALUES (2276,'17',17,'report.quality.dailyuseraway','上上月当月日累计注销飞信用户数','',NULL,'80','center','true',1,2271);
INSERT INTO `T_FETION_COLUMNS` VALUES (1630,'1',1,'report.usernumber.dailyallactiveusernumber','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1631,'2',2,'report.usernumber.dailyallactiveusernumber','当日飞信活跃用户数','activeUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1632,'3',3,'report.usernumber.dailyallactiveusernumber','当日非CM PC活跃用户数','notCMPCUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1633,'4',4,'report.usernumber.dailyallactiveusernumber','当日大众版活跃用户数','publicActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1634,'5',5,'report.usernumber.dailyallactiveusernumber','当日海外版活跃用户数','abroadActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1635,'6',6,'report.usernumber.dailyallactiveusernumber','当日活跃企业用户数','enterpriseActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1636,'7',7,'report.usernumber.dailyallactiveusernumber','当日飞信累计活跃用户数','totalActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1637,'8',8,'report.usernumber.dailyallactiveusernumber','当日非CM累计PC活跃用户数','totalNotCMPCUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1638,'9',9,'report.usernumber.dailyallactiveusernumber','当日大众版累计活跃用户数','totalPublicActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1639,'10',10,'report.usernumber.dailyallactiveusernumber','当日海外版累计活跃用户数','totalAbroadActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1640,'11',11,'report.usernumber.dailyallactiveusernumber','当日累计活跃企业用户数','totalEnterpriseActiveUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1650,'1',1,'report.usernumber.dailyallproductlineactive','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1651,'2',2,'report.usernumber.dailyallproductlineactive','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1652,'3',3,'report.usernumber.dailyallproductlineactive','全产品线活跃用户','activeUserNumber',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1653,'2',2,'report.usernumber.dailyallproductlineactive','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2290,'1',1,'report.quality.monthlyfriend','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2291,'2',2,'report.quality.monthlyfriend','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2292,'3',3,'report.quality.monthlyfriend','0-1好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2293,'4',4,'report.quality.monthlyfriend','2-5好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2294,'5',5,'report.quality.monthlyfriend','6-15好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2295,'6',6,'report.quality.monthlyfriend','16-23好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2296,'7',7,'report.quality.monthlyfriend','24-70好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2297,'8',8,'report.quality.monthlyfriend','70以上','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2298,'2',2,'report.quality.monthlyfriend','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2310,'1',1,'report.quality.monthlyactiveday','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2311,'2',2,'report.quality.monthlyactiveday','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2312,'3',3,'report.quality.monthlyactiveday','1天','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2313,'4',4,'report.quality.monthlyactiveday','2-5天','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2314,'5',5,'report.quality.monthlyactiveday','6-15天','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2315,'6',6,'report.quality.monthlyactiveday','15天以上','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2316,'2',2,'report.quality.monthlyactiveday','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2320,'1',1,'report.quality.monthlyqualityanalyze','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2321,'2',2,'report.quality.monthlyqualityanalyze','手机活跃用户的活跃质量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2322,'3',3,'report.quality.monthlyqualityanalyze','当月手机客户端活跃用户人均好友数','',NULL,'80','center','true',1,2321);
INSERT INTO `T_FETION_COLUMNS` VALUES (2323,'4',4,'report.quality.monthlyqualityanalyze','当月手机活跃人均手机联系好友数','',NULL,'80','center','true',1,2321);
INSERT INTO `T_FETION_COLUMNS` VALUES (2324,'5',5,'report.quality.monthlyqualityanalyze','当月手机飞信活跃用户人均活跃天数','',NULL,'80','center','true',1,2321);
INSERT INTO `T_FETION_COLUMNS` VALUES (2325,'6',6,'report.quality.monthlyqualityanalyze','当月手机活跃人均手机在线时长','',NULL,'80','center','true',1,2321);
INSERT INTO `T_FETION_COLUMNS` VALUES (2326,'7',7,'report.quality.monthlyqualityanalyze','手机飞信活跃用户的活跃质量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2327,'8',8,'report.quality.monthlyqualityanalyze','当月手机飞信活跃用户人均好友数','',NULL,'80','center','true',1,2326);
INSERT INTO `T_FETION_COLUMNS` VALUES (2328,'9',9,'report.quality.monthlyqualityanalyze','当月手机飞信活跃人均手机飞信联系好友数','',NULL,'80','center','true',1,2326);
INSERT INTO `T_FETION_COLUMNS` VALUES (2329,'10',10,'report.quality.monthlyqualityanalyze','当月人均手机飞信活跃天数','',NULL,'80','center','true',1,2326);
INSERT INTO `T_FETION_COLUMNS` VALUES (2330,'11',11,'report.quality.monthlyqualityanalyze','当月人均手机飞信在线时长','',NULL,'80','center','true',1,2326);
INSERT INTO `T_FETION_COLUMNS` VALUES (2331,'12',12,'report.quality.monthlyqualityanalyze','当月人均手机飞信消息量','',NULL,'80','center','true',1,2326);
INSERT INTO `T_FETION_COLUMNS` VALUES (2332,'13',13,'report.quality.monthlyqualityanalyze','当月人均手机飞信短信量','',NULL,'80','center','true',1,2326);
INSERT INTO `T_FETION_COLUMNS` VALUES (2333,'14',14,'report.quality.monthlyqualityanalyze','WAP飞信活跃用户的活跃质量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2334,'15',15,'report.quality.monthlyqualityanalyze','当月WAP飞信活跃用户的人均好友量','',NULL,'80','center','true',1,2333);
INSERT INTO `T_FETION_COLUMNS` VALUES (2335,'16',16,'report.quality.monthlyqualityanalyze','当月WAP飞信活跃人均WAP联系好友数','',NULL,'80','center','true',1,2333);
INSERT INTO `T_FETION_COLUMNS` VALUES (2336,'17',17,'report.quality.monthlyqualityanalyze','当月人均WAP飞信活跃天数','',NULL,'80','center','true',1,2333);
INSERT INTO `T_FETION_COLUMNS` VALUES (2337,'18',18,'report.quality.monthlyqualityanalyze','当月人均WAP飞信在线时长','',NULL,'80','center','true',1,2333);
INSERT INTO `T_FETION_COLUMNS` VALUES (2338,'19',19,'report.quality.monthlyqualityanalyze','当月人均WAP飞信消息量','',NULL,'80','center','true',1,23330);
INSERT INTO `T_FETION_COLUMNS` VALUES (2339,'20',20,'report.quality.monthlyqualityanalyze','当月人均WAP飞信短信量','',NULL,'80','center','true',1,2333);
INSERT INTO `T_FETION_COLUMNS` VALUES (2350,'1',1,'report.quality.monthlyusersource','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2351,'2',2,'report.quality.monthlyusersource','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2352,'3',3,'report.quality.monthlyusersource','客户端','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2353,'4',4,'report.quality.monthlyusersource','当月稽核后手机飞信活跃用户在本月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2354,'5',5,'report.quality.monthlyusersource','当月稽核后手机飞信活跃用户数','',NULL,'80','center','true',1,2353);
INSERT INTO `T_FETION_COLUMNS` VALUES (2355,'6',6,'report.quality.monthlyusersource','稽核后新开通手机飞信活跃用户数','',NULL,'80','center','true',1,2353);
INSERT INTO `T_FETION_COLUMNS` VALUES (2356,'7',7,'report.quality.monthlyusersource','稽核后手机飞信活跃且其他客户端活跃用户数','',NULL,'80','center','true',1,2353);
INSERT INTO `T_FETION_COLUMNS` VALUES (2357,'8',8,'report.quality.monthlyusersource','当月稽核后手机飞信活跃用户在上月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2358,'9',9,'report.quality.monthlyusersource','稽核后连续2个月手机飞信活跃用户数','',NULL,'80','center','true',1,2357);
INSERT INTO `T_FETION_COLUMNS` VALUES (2359,'10',10,'report.quality.monthlyusersource','稽核后上月非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2357);
INSERT INTO `T_FETION_COLUMNS` VALUES (2360,'11',11,'report.quality.monthlyusersource','稽核后上月飞信不活跃用户数','',NULL,'80','center','true',1,2357);
INSERT INTO `T_FETION_COLUMNS` VALUES (2361,'12',12,'report.quality.monthlyusersource','当月稽核后手机飞信活跃用户在上上月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2362,'13',13,'report.quality.monthlyusersource','稽核后连续3个月手机飞信活跃用户数','',NULL,'80','center','true',1,2361);
INSERT INTO `T_FETION_COLUMNS` VALUES (2363,'14',14,'report.quality.monthlyusersource','稽核后上上月、当月手机飞信活跃用户数','',NULL,'80','center','true',1,2361);
INSERT INTO `T_FETION_COLUMNS` VALUES (2364,'15',15,'report.quality.monthlyusersource','稽核后上上月非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2361);
INSERT INTO `T_FETION_COLUMNS` VALUES (2365,'16',16,'report.quality.monthlyusersource','稽核后上上月飞信不活跃用户数','',NULL,'80','center','true',1,2361);
INSERT INTO `T_FETION_COLUMNS` VALUES (2380,'1',1,'report.quality.monthlyuseraway','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2381,'2',2,'report.quality.monthlyuseraway','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2382,'3',3,'report.quality.monthlyuseraway','客户端','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2383,'4',4,'report.quality.monthlyuseraway','上月稽核后手机飞信活跃用户在当月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2384,'5',5,'report.quality.monthlyuseraway','上月稽核后当月手机飞信活跃用户数','',NULL,'80','center','true',1,2383);
INSERT INTO `T_FETION_COLUMNS` VALUES (2385,'6',6,'report.quality.monthlyuseraway','上月稽核后当月非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2383);
INSERT INTO `T_FETION_COLUMNS` VALUES (2386,'7',7,'report.quality.monthlyuseraway','上月稽核后当月飞信不活跃用户数','',NULL,'80','center','true',1,2383);
INSERT INTO `T_FETION_COLUMNS` VALUES (2387,'8',8,'report.quality.monthlyuseraway','上月稽核后当月注销飞信用户数','',NULL,'80','center','true',1,2383);
INSERT INTO `T_FETION_COLUMNS` VALUES (2388,'9',9,'report.quality.monthlyuseraway','上上月稽核后手机飞信活跃用户在当月活跃情况','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2389,'10',10,'report.quality.monthlyuseraway','上上月稽核后连续3个月手机飞信活跃的用户数','',NULL,'80','center','true',1,2388);
INSERT INTO `T_FETION_COLUMNS` VALUES (2390,'11',11,'report.quality.monthlyuseraway','上上月稽核后当月手机飞信活跃用户数','',NULL,'80','center','true',1,2388);
INSERT INTO `T_FETION_COLUMNS` VALUES (2391,'12',12,'report.quality.monthlyuseraway','上上月稽核后当月非手机飞信活跃的活跃用户数','',NULL,'80','center','true',1,2388);
INSERT INTO `T_FETION_COLUMNS` VALUES (2392,'13',13,'report.quality.monthlyuseraway','上上月稽核后当月飞信不活跃用户数','',NULL,'80','center','true',1,2388);
INSERT INTO `T_FETION_COLUMNS` VALUES (2393,'14',14,'report.quality.monthlyuseraway','上上月稽核后当月注销飞信用户数','',NULL,'80','center','true',1,2388);

INSERT INTO `T_FETION_COLUMNS` VALUES (2070,'1',1,'report.cooperationchannel.dailychannelmobileclient','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2071,'2',2,'report.cooperationchannel.dailychannelmobileclient','OEMTAG','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2072,'3',3,'report.cooperationchannel.dailychannelmobileclient','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2073,'4',4,'report.cooperationchannel.dailychannelmobileclient','当日手机飞信渠道累计活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2074,'5',5,'report.cooperationchannel.dailychannelmobileclient','当日手机飞信渠道活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2075,'6',6,'report.cooperationchannel.dailychannelmobileclient','当日手机飞信渠道开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2076,'7',7,'report.cooperationchannel.dailychannelmobileclient','当日累计手机飞信渠道开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2077,'8',8,'report.cooperationchannel.dailychannelmobileclient','当日手机渠道首次开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2078,'9',9,'report.cooperationchannel.dailychannelmobileclient','当日手机渠道累计注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2079,'10',10,'report.cooperationchannel.dailychannelmobileclient','当日渠道退订用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2080,'11',11,'report.cooperationchannel.dailychannelmobileclient','当日手机飞信渠道登录用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2081,'12',12,'report.cooperationchannel.dailychannelmobileclient','当日手机飞信渠道登录次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2082,'13',13,'report.cooperationchannel.dailychannelmobileclient','当日稽核后手机飞信渠道累计活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2090,'1',1,'report.cooperationchannel.weeklyclientlogin','OEM TAG','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2091,'2',2,'report.cooperationchannel.weeklyclientlogin','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2092,'3',3,'report.cooperationchannel.weeklyclientlogin','本周手机客户端Tag登录用户','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2093,'4',4,'report.cooperationchannel.weeklyclientlogin','本周手机客户端登Tag录次数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2094,'2',2,'report.cooperationchannel.weeklyclientlogin','城市','city',NULL,'80','center','false',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2100,'1',1,'report.cooperationchannel.monthlychannelmobileclient','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2101,'2',2,'report.cooperationchannel.monthlychannelmobileclient','OEMTAG','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2102,'3',3,'report.cooperationchannel.monthlychannelmobileclient','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2103,'4',4,'report.cooperationchannel.monthlychannelmobileclient','当月手机飞信渠道活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2104,'5',5,'report.cooperationchannel.monthlychannelmobileclient','当月手机飞信渠道活跃零好友用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2105,'6',6,'report.cooperationchannel.monthlychannelmobileclient','当月手机飞信渠道活跃零消息用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2106,'7',7,'report.cooperationchannel.monthlychannelmobileclient','当月手机飞信渠道活跃双零用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2107,'8',8,'report.cooperationchannel.monthlychannelmobileclient','当月手机飞信渠道登录次数','',NULL,'80','center','true',1,0);

INSERT INTO `T_FETION_COLUMNS` VALUES (2000,'1',1,'report.other.dailyactivescalechange','省份','province',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2001,'2',2,'report.other.dailyactivescalechange','','number1',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2002,'3',3,'report.other.dailyactivescalechange','','number2',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2003,'4',4,'report.other.dailyactivescalechange','变化幅度','changeMargin',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2004,'5',5,'report.other.dailyactivescalechange','变化率','changeRate',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2020,'1',1,'report.other.dailyflowmanageanalyze','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2021,'2',2,'report.other.dailyflowmanageanalyze','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2022,'3',3,'report.other.dailyflowmanageanalyze','传输类型','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2023,'4',4,'report.other.dailyflowmanageanalyze','上行流量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2024,'5',5,'report.other.dailyflowmanageanalyze','下行流量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2030,'1',1,'report.other.monthlydayactivecompare','','date1',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2031,'2',2,'report.other.monthlydayactivecompare','手机活跃用户数(万)','number1',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2032,'3',3,'report.other.monthlydayactivecompare','','date2',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2033,'4',4,'report.other.monthlydayactivecompare','手机活跃用户数(万)','number2',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2034,'5',5,'report.other.monthlydayactivecompare','','date3',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2035,'6',6,'report.other.monthlydayactivecompare','手机活跃用户数(万)','number3',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2050,'1',1,'report.other.monthlytotaldayactivecompare','','date1',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2051,'2',2,'report.other.monthlytotaldayactivecompare','手机日累计活跃用户数(万)','number1',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2052,'3',3,'report.other.monthlytotaldayactivecompare','','date2',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2053,'4',4,'report.other.monthlytotaldayactivecompare','手机日累计活跃用户数(万)','number2',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2054,'5',5,'report.other.monthlytotaldayactivecompare','','date3',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (2055,'6',6,'report.other.monthlytotaldayactivecompare','手机日累计活跃用户数(万)','number3',NULL,'80','center','true',1,0);

INSERT INTO `T_FETION_COLUMNS` VALUES (1880,'1',1,'report.usernumberanalyze.dailyfetionusernumber','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1881,'2',2,'report.usernumberanalyze.dailyfetionusernumber','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1882,'3',3,'report.usernumberanalyze.dailyfetionusernumber','当日累计注册用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1883,'4',4,'report.usernumberanalyze.dailyfetionusernumber','当日到达用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1884,'5',5,'report.usernumberanalyze.dailyfetionusernumber','当日累计活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1885,'6',6,'report.usernumberanalyze.dailyfetionusernumber','当日活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1886,'7',7,'report.usernumberanalyze.dailyfetionusernumber','当日手机飞信活跃用户数 ','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1887,'8',8,'report.usernumberanalyze.dailyfetionusernumber','当日开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1888,'9',9,'report.usernumberanalyze.dailyfetionusernumber','当日批量开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1889,'10',10,'report.usernumberanalyze.dailyfetionusernumber','当日净增用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1890,'11',11,'report.usernumberanalyze.dailyfetionusernumber','当日注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1891,'12',12,'report.usernumberanalyze.dailyfetionusernumber','当日主动注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1892,'13',13,'report.usernumberanalyze.dailyfetionusernumber','当日被动注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1900,'1',1,'report.usernumberanalyze.dailyuseractive','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1901,'2',2,'report.usernumberanalyze.dailyuseractive','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1902,'3',3,'report.usernumberanalyze.dailyuseractive','当日飞信累计活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1903,'4',4,'report.usernumberanalyze.dailyuseractive','当日飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1904,'5',5,'report.usernumberanalyze.dailyuseractive','当日客户端累计活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1905,'6',6,'report.usernumberanalyze.dailyuseractive','当日客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1906,'7',7,'report.usernumberanalyze.dailyuseractive','当日PC活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1907,'8',8,'report.usernumberanalyze.dailyuseractive','当日手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1908,'9',9,'report.usernumberanalyze.dailyuseractive','当日短信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1909,'10',10,'report.usernumberanalyze.dailyuseractive','当日飞信累计活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1910,'11',11,'report.usernumberanalyze.dailyuseractive','当日IVR活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1911,'12',12,'report.usernumberanalyze.dailyuseractive','当日客户端消息条数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1912,'13',13,'report.usernumberanalyze.dailyuseractive','当日客户端短信条数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1913,'14',14,'report.usernumberanalyze.dailyuseractive','当日累计双1用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1914,'15',15,'report.usernumberanalyze.dailyuseractive','当日累计低频次用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1915,'16',16,'report.usernumberanalyze.dailyuseractive','人均消息量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1916,'17',17,'report.usernumberanalyze.dailyuseractive','人均短信量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1930,'1',1,'report.usernumberanalyze.dailyallnetonlineusernumber','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1931,'2',2,'report.usernumberanalyze.dailyallnetonlineusernumber','同时在线用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1940,'1',1,'report.usernumberanalyze.dailycommonproductstonline','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1941,'2',2,'report.usernumberanalyze.dailycommonproductstonline','时点','time',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1946,'7',7,'report.usernumberanalyze.dailycommonproductstonline','Mini飞信客户端','peak',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1960,'1',1,'report.usernumberanalyze.monthlyuser','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1961,'2',2,'report.usernumberanalyze.monthlyuser','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1962,'3',3,'report.usernumberanalyze.monthlyuser','当月累计注册用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1963,'4',4,'report.usernumberanalyze.monthlyuser','当月到达用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1964,'5',5,'report.usernumberanalyze.monthlyuser','当月活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1965,'6',6,'report.usernumberanalyze.monthlyuser','当月手机飞信活跃用户数 ','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1966,'7',7,'report.usernumberanalyze.monthlyuser','当月开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1967,'8',8,'report.usernumberanalyze.monthlyuser','当月净增用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1968,'9',9,'report.usernumberanalyze.monthlyuser','当月批量开通用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1969,'10',10,'report.usernumberanalyze.monthlyuser','当月注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1970,'11',11,'report.usernumberanalyze.monthlyuser','当月主动注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1971,'12',12,'report.usernumberanalyze.monthlyuser','当月被动注销用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1980,'1',1,'report.usernumberanalyze.monthlyuseractive','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1981,'2',2,'report.usernumberanalyze.monthlyuseractive','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1982,'3',3,'report.usernumberanalyze.monthlyuseractive','当月活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1983,'4',4,'report.usernumberanalyze.monthlyuseractive','当月客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1984,'5',5,'report.usernumberanalyze.monthlyuseractive','当月PC活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1985,'6',6,'report.usernumberanalyze.monthlyuseractive','当月手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1986,'7',7,'report.usernumberanalyze.monthlyuseractive','当月短信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1987,'8',8,'report.usernumberanalyze.monthlyuseractive','当月IVR活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1988,'9',9,'report.usernumberanalyze.monthlyuseractive','当月客户端消息条数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1989,'10',10,'report.usernumberanalyze.monthlyuseractive','当月客户端短信条数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1990,'11',11,'report.usernumberanalyze.monthlyuseractive','当月累计双1用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1991,'12',12,'report.usernumberanalyze.monthlyuseractive','当月累计低频次用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1992,'13',13,'report.usernumberanalyze.monthlyuseractive','人均消息量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1993,'14',14,'report.usernumberanalyze.monthlyuseractive','人均短信量','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1820,'1',1,'report.usernumber.monthlyonefriend','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1821,'2',2,'report.usernumber.monthlyonefriend','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1822,'3',3,'report.usernumber.monthlyonefriend','月小于等于1好友到达用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1823,'4',4,'report.usernumber.monthlyonefriend','月小于等于1好友的活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1824,'5',5,'report.usernumber.monthlyonefriend','月小于等于1好友的活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1825,'6',6,'report.usernumber.monthlyonefriend','月小于等于1好友的PC客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1826,'7',7,'report.usernumber.monthlyonefriend','月小于等于1好友剔除单一WAP飞信的手机飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1827,'8',8,'report.usernumber.monthlyonefriend','月小于等于1好友的WAP飞信客户端活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1828,'9',9,'report.usernumber.monthlyonefriend','月小于等于1好友的单一WAP飞信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1829,'10',10,'report.usernumber.monthlyonefriend','月小于等于1好友的短信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1830,'11',11,'report.usernumber.monthlyonefriend','月小于等于1好友的短信活跃用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1840,'1',1,'report.usernumber.monthlyonefriendactive','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1841,'2',2,'report.usernumber.monthlyonefriendactive','活跃用户中1或0好友的用户数(B类)','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1842,'3',3,'report.usernumber.monthlyonefriendactive','PC活跃用户中1或0好友的用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1843,'4',4,'report.usernumber.monthlyonefriendactive','手机飞信活跃用户中1或0好友的用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1844,'5',5,'report.usernumber.monthlyonefriendactive','WAP飞信活跃用户中1或0好友的用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1845,'6',6,'report.usernumber.monthlyonefriendactive','短信活跃用户中1或0好友的用户数(C类)','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1846,'7',7,'report.usernumber.monthlyonefriendactive','纯短信活跃1天次用户中,其活跃短信条数<=功能性指令短信条数的(D类)','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1847,'8',8,'report.usernumber.monthlyonefriendactive','活跃用户中稽核用户总数(B类用户和D类用户排重)','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1848,'9',9,'report.usernumber.monthlyonefriendactive','短信活跃用户中稽核用户总数(C类用户和D类用户排重)','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1849,'10',10,'report.usernumber.monthlyonefriendactive','拇指群活跃的用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1860,'1',1,'report.usernumber.monthlybusiness','日期','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1861,'2',2,'report.usernumber.monthlybusiness','业务量类型','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1862,'3',3,'report.usernumber.monthlybusiness','活跃类型','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1863,'4',4,'report.usernumber.monthlybusiness','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1864,'5',5,'report.usernumber.monthlybusiness','运营商','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1865,'6',6,'report.usernumber.monthlybusiness','0条','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1866,'7',7,'report.usernumber.monthlybusiness','1条 ','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1867,'8',8,'report.usernumber.monthlybusiness','2-5条','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1868,'9',9,'report.usernumber.monthlybusiness','6-10条','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1869,'10',10,'report.usernumber.monthlybusiness','11-20条','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1870,'11',11,'report.usernumber.monthlybusiness','21-50条','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1871,'12',12,'report.usernumber.monthlybusiness','51-100条 ','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1872,'13',13,'report.usernumber.monthlybusiness','101条以上','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1260,'1',1,'report.memberanalyze.monthlyfriends','月份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1261,'2',2,'report.memberanalyze.monthlyfriends','省份','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1262,'3',3,'report.memberanalyze.monthlyfriends','0-50个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1263,'4',4,'report.memberanalyze.monthlyfriends','51-个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1264,'5',5,'report.memberanalyze.monthlyfriends','101-150个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1265,'6',6,'report.memberanalyze.monthlyfriends','151-200个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1266,'7',7,'report.memberanalyze.monthlyfriends','201-250个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1267,'8',8,'report.memberanalyze.monthlyfriends','251-300个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1268,'9',9,'report.memberanalyze.monthlyfriends','301-400个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1269,'10',10,'report.memberanalyze.monthlyfriends','401-500个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1270,'11',11,'report.memberanalyze.monthlyfriends','501-800个好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (1271,'12',12,'report.memberanalyze.monthlyfriends','801个以上好友','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (500,'1',1,'report.differentnet.dailyregister','日期','date',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (501,'2',2,'report.differentnet.dailyregister','运营商','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (502,'3',3,'report.differentnet.dailyregister','当日累计异网注册用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (503,'4',4,'report.differentnet.dailyregister','当日开通异网用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (504,'5',5,'report.differentnet.dailyregister','当日PC主客户端开通异网用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (505,'6',6,'report.differentnet.dailyregister','当日手机开通异网用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (506,'7',7,'report.differentnet.dailyregister','当日WAP开通异网用户数 ','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (507,'8',8,'report.differentnet.dailyregister','当日官网开通异网用户数','',NULL,'80','center','true',1,0);
INSERT INTO `T_FETION_COLUMNS` VALUES (508,'9',9,'report.differentnet.dailyregister','当日注销异网用户数','',NULL,'80','center','true',1,0);

