-- CREATE DATABASE online_judge
-- USE online_judge

DROP TABLE IF EXISTS `test_sample`;
DROP TABLE IF EXISTS `solution`;
DROP TABLE IF EXISTS `submission`;
DROP TABLE IF EXISTS `io`;
DROP TABLE IF EXISTS `problem`;
DROP TABLE IF EXISTS `user`;




CREATE TABLE `problem` (
	`problemKey`		INT				NOT NULL AUTO_INCREMENT COMMENT '题目编号',
	`problemName`		VARCHAR(256) 	NOT NULL 				COMMENT '题目标题',
	`source`			VARCHAR(128)							COMMENT '出题人/题目来源',
	`description`		VARCHAR(2048)	NOT NULL 				COMMENT '题目描述',
	`timeLimit`			INT				NOT NULL				COMMENT '题目时限',
	`memoryLimit`		INT				NOT NULL				COMMENT '内存限制',
	`testpoint`			INT				NOT	NULL				COMMENT '测试点数量',
	`tag`				VARCHAR(128)							COMMENT '标签',
	
	PRIMARY KEY (`problemKey`)
);


CREATE TABLE `user` (
	`id`				INT					NOT NULL AUTO_INCREMENT COMMENT '用户编号',
	`userName`			VARCHAR(128)		NOT NULL				COMMENT '用户名/账号',
	`password`			VARCHAR(128)		NOT NULL				COMMENT '用户密码',
	`avatarURI`			VARCHAR(128)								COMMENT '头像路径(预留)',
	
	PRIMARY KEY (`id`)
);


CREATE TABLE `io` (
	`ioKey`				INT				NOT NULL AUTO_INCREMENT COMMENT '测试用例编号',
	`problemKey`		INT				NOT NULL 				COMMENT '所属题目',
	`input`				VARCHAR(1024)	NOT NULL 				COMMENT '输入',
	`output`			VARCHAR(1024)	NOT NULL 				COMMENT '输出',
	
	PRIMARY KEY (`ioKey`),
	CONSTRAINT `_fk_1` FOREIGN KEY (`problemKey`) 	REFERENCES `problem`(`problemKey`)
);


CREATE TABLE `submission` (
	`submissionKey`		INT				NOT NULL AUTO_INCREMENT COMMENT '提交记录编号',
	`problemKey`		INT				NOT NULL 				COMMENT '题目编号',
	`userKey`			INT 			NOT NULL				COMMENT '所属用户',
	`time`				TIMESTAMP		NOT NULL				COMMENT '提交时间',
	`result`			VARCHAR(64)		NOT NULL				COMMENT '编译结果',
	`runtime`			INT				NOT NULL				COMMENT '运行用时',
	`language`			VARCHAR(64)		NOT NULL				COMMENT '提交使用语言',
	
	PRIMARY KEY (`submissionKey`),
	CONSTRAINT `submission_fk_1` FOREIGN KEY (`userKey`) 	REFERENCES `user`(`id`)
);

CREATE TABLE `solution` (
	`solutionKey`		INT				NOT NULL AUTO_INCREMENT COMMENT '题解编号',
	`problemKey`		INT				NOT NULL				COMMENT '对应题号',
	`userKey`			INT				NOT NULL				COMMENT '贡献者编号',
	`time`				DATE			NOT NULL				COMMENT '提交时间',
	`content`			VARCHAR(2048)	NOT NULL				COMMENT '正文',

	PRIMARY KEY (`solutionKey`),
	CONSTRAINT `solution_fk_1` FOREIGN KEY (`problemKey`)	REFERENCES `problem`(`problemKey`)
)