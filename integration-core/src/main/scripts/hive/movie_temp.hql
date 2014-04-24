--电影临时表
CREATE TABLE MOVIE_TEMP (
ID BIGINT COMMENT '电影ID', 
NAME STRING COMMENT '电影名称', 
TYPE STRING COMMENT '电影类型')
COMMENT '电影临时表'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ':'
STORED AS 
INPUTFORMAT 'platform.utils.bigdata.hive.NewInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat';

LOAD DATA INPATH '/home/hadoop/filesystem/data/movie/movies.dat' 
INTO TABLE MOVIE_TEMP;

--用户临时表
CREATE TABLE USER_TEMP (
ID BIGINT COMMENT '用户ID',
SEX STRING COMMENT '用户性别',
AGE INT COMMENT '用户年龄',
JOB STRING COMMENT '用户工作标识',
ZIPCODE STRING COMMENT '邮政编码')
COMMENT '用户临时表'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ':'
STORED AS 
INPUTFORMAT 'platform.utils.bigdata.hive.NewInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat';

LOAD DATA INPATH '/home/hadoop/filesystem/data/movie/users.dat' 
INTO TABLE USER_TEMP;

--评分临时表
CREATE TABLE RATING_TEMP (
UID BIGINT COMMENT '用户ID',
MID BIGINT COMMENT '电影ID',
RATING STRING COMMENT '评分',
TIMESTAMP STRING COMMENT '时间戳')
COMMENT '评分临时表'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ':'
STORED AS
INPUTFORMAT 'platform.utils.bigdata.hive.NewInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat';

LOAD DATA INPATH '/home/hadoop/filesystem/data/movie/ratings.dat' 
INTO TABLE RATING_TEMP;

--标记临时表
CREATE TABLE TAG_TEMP (
UID BIGINT COMMENT '用户ID',
MID BIGINT COMMENT '电影ID',
TAG STRING COMMENT '标记',
TIMESTAMP STRING COMMENT '时间戳')
COMMENT '标记临时表'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ':'
STORED AS
INPUTFORMAT 'platform.utils.bigdata.hive.NewInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat';

LOAD DATA INPATH '/home/hadoop/filesystem/data/movie/tags.dat' 
INTO TABLE TAG_TEMP;