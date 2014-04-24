CREATE DATABASE MAHOUT;   
USE MAHOUT;

--保存电影相关的信息
CREATE TABLE movies (  
    id INTEGER NOT NULL AUTO_INCREMENT, 
    name varchar(100) NOT NULL, 
    published_year varchar(4) default NULL, 
    type varchar(100) default NULL,  
    PRIMARY KEY (id) 
); 

--保存用户信息
CREATE TABLE users (  
    id INTEGER NOT NULL AUTO_INCREMENT, 
    name varchar(50) NOT NULL, 
    sex varchar(10),
    age int,
    job varchar(100),
    zipcode varchar(20),
    email varchar(100) default NULL, 
    PRIMARY KEY (id) 
); 

--保存用户对电影的评分，即喜好程度
CREATE TABLE movie_preferences ( 
    userID INTEGER NOT NULL, 
    movieID INTEGER NOT NULL, 
    preference INTEGER NOT NULL DEFAULT 0, 
    timestamp INTEGER not null default 0, 
    FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE, 
    FOREIGN KEY (movieID) REFERENCES movies(id) ON DELETE CASCADE 
); 

--保存电影和电影的相似程度
CREATE TABLE movie_similarity ( 
    movieID1 INTEGER NOT NULL, 
    movieID2 INTEGER NOT NULL, 
    similarity DOUBLE NOT NULL DEFAULT 0, 
    FOREIGN KEY (movieID1) REFERENCES movies(id) ON DELETE CASCADE, 
    FOREIGN KEY (movieID2) REFERENCES movies(id) ON DELETE CASCADE 
); 

CREATE INDEX movie_preferences_index1 ON movie_preferences ( userID , movieID ); 
CREATE INDEX movie_preferences_index2 ON movie_preferences ( userID ); 
CREATE INDEX movie_preferences_index3 ON movie_preferences ( movieID );