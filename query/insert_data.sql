USE online_judge
SET	GLOBAL LOCAL_INFILE = 1;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\sms\\problem.tbl' INTO TABLE `problem`
	FIELDS TERMINATED BY '|' LINES TERMINATED BY '\r\n';
LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\sms\\user.tbl' INTO TABLE `user`
	FIELDS TERMINATED BY '|' LINES TERMINATED BY '\r\n';
LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\sms\\io.tbl' INTO TABLE `io` 
	FIELDS TERMINATED BY '|' LINES TERMINATED BY '\r\n';
LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\sms\\submission.tbl' INTO TABLE `submission` 
	FIELDS TERMINATED BY '|' LINES TERMINATED BY '\r\n';
LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\sms\\solution.tbl' INTO TABLE `solution` 
	FIELDS TERMINATED BY '|' LINES TERMINATED BY '\r\n';