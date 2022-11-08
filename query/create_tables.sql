CREATE DATABASE online_judge
USE online_judge
DROP TABLE IF EXISTS `problem`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `test_sample`;
DROP TABLE IF EXISTS `submission`;
DROP TABLE IF EXISTS `solution`;
DROP TABLE IF EXISTS `chating`;
DROP TABLE IF EXISTS `friends`;



CREATE TABLE `problem` (
	`problemKey`		INT				NOT NULL AUTO_INCREMENT COMMENT '题目编号',
	`problemName`		VARCHAR(64) 	NOT NULL 				COMMENT '题目标题',
	`source`			VARCHAR(64)								COMMENT '出题人/题目来源',
	`description`		VARCHAR(512)	NOT NULL 				COMMENT '题目描述json',
	`sampleIO`			VARCHAR(128)							COMMENT '示例输入输出json',
	`tag`				VARCHAR(64)								COMMENT '标签json',
	
	PRIMARY KEY (`problemKey`)
);


CREATE TABLE `user` (
	`id`				INT				NOT NULL AUTO_INCREMENT COMMENT '用户编号',
	`userName`			VARCHAR(128)		NOT NULL				COMMENT '用户名/账号',
	`password`			VARCHAR(128)		NOT NULL				COMMENT '用户密码',
	`avatarURI`			VARCHAR(128)								COMMENT '头像路径(预留)',
	
	PRIMARY KEY (`id`)
);


CREATE TABLE `test_sample` (
	`sampleKey`			INT				NOT NULL AUTO_INCREMENT COMMENT '测试用例编号',
	`problemKey`		INT				NOT NULL 				COMMENT '所属题目',
	`IOpair`			VARCHAR(512)	NOT NULL 				COMMENT '输入输出用例对json',
	
	PRIMARY KEY (`sampleKey`),
	CONSTRAINT `sample_fk_1` FOREIGN KEY (`problemKey`) 	REFERENCES `problem`(`problemKey`)
);


CREATE TABLE `submission` (
	`submissionKey`		INT				NOT NULL AUTO_INCREMENT COMMENT '提交记录编号',
	`userKey`			INT 			NOT NULL				COMMENT '所属用户',
	`time`				TIMESTAMP		NOT NULL				COMMENT '提交时间',
	`result`			ENUM(
						 'Accepted',
						 'WrongAnswer',
						 'Pending',
						 'CompileError',
						 'TimeLimitExceeded',
						 'RuntimeError',
						 'MemoryLimitExceeded')
										NOT NULL				COMMENT '结果/状态',
	`runtime`			INT				NOT NULL				COMMENT '运行用时',
	`language`			ENUM(
						 'Java',
						 'Python',
						 'C++')			NOT NULL				COMMENT '提交使用语言',
	
	PRIMARY KEY (`submissionKey`)
);

CREATE TABLE `solution` (
	`solutionKey`		INT				NOT NULL AUTO_INCREMENT COMMENT '题解编号',
	`problemKey`		INT				NOT NULL				COMMENT '对应题号',
	`userKey`			INT				NOT NULL				COMMENT '贡献者编号',
	`userName`			VARCHAR(32)		NOT NULL				COMMENT '用户名',
	`time`				DATE			NOT NULL				COMMENT '提交时间',
	`title`				VARCHAR(64)		NOT NULL				COMMENT '题解标题',
	`overview`			VARCHAR(64)		NOT NULL				COMMENT '题解概要',
	`content`			VARCHAR(2048)	NOT NULL				COMMENT '正文',
	`language`			VARCHAR(64)		NOT NULL				COMMENT '题解语言',

	PRIMARY KEY (`solutionKey`)
);

CREATE TABLE `chatting` (
	`chatKey`			INT				NOT NULL AUTO_INCREMENT ,
	`senderKey`			INT				NOT NULL,
	`senderName`		VARCHAR(32)		NOT NULL,				
	`receiverKey`		INT				NOT NULL,				
	`receiverName`		VARCHAR(32)		NOT NULL,
	`content`			VARCHAR(1024)	NOT NULL,
	`time`				TIMESTAMP		NOT NULL,
	`state`				VARCHAR(16)		NOT NULL,

	PRIMARY KEY (`chatKey`)
);

CREATE Table friends(
 	`userKey` 			INT				NOT NULL,
	`userName`			VARCHAR(32) 	NOT NULL,
 	`friends` 			VARCHAR(1024) 	NOT NULL,
 
 	PRIMARY KEY ( `userKey` ) 
);