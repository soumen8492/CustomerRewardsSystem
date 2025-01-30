DROP TABLE IF EXISTS CUSTOMERTRANSACTION;
CREATE TABLE CUSTOMERTRANSACTION (
tran_id INT AUTO_INCREMENT  PRIMARY KEY,
cust_id INT,
name VARCHAR(10),
tran_date VARCHAR(15),
amount INT
);
DROP TABLE IF EXISTS REWARDPOINTS;
CREATE TABLE REWARDPOINTS (
reward_id VARCHAR(15) PRIMARY KEY,
cust_id VARCHAR(10),
name VARCHAR(10),
mnth VARCHAR(15),
points INT,
amount_spent INT
);
