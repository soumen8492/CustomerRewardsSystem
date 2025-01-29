DROP TABLE IF EXISTS CUSTOMERTRANSACTION;
CREATE TABLE CUSTOMERTRANSACTION (
tran_id INT AUTO_INCREMENT  PRIMARY KEY,
cust_id INT,
name VARCHAR(10),
tran_date VARCHAR(10),
amount INT
);
DROP TABLE IF EXISTS REWARDPOINTS;
CREATE TABLE REWARDPOINTS (
reward_id VARCHAR(5) PRIMARY KEY,
cust_id INT,
name VARCHAR(10),
mnth VARCHAR(3),
points INT
);