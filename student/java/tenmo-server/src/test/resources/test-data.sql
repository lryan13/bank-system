BEGIN TRANSACTION;
DROP TABLE IF EXISTS users, accounts, transfers CASCADE;
CREATE TABLE users (
	user_id varchar(50),
	username varchar(50) NOT NULL,
	password_hash char(50) NOT NULL,
	CONSTRAINT PK_users PRIMARY KEY(user_id),
	CONSTRAINT UQ_username UNIQUE(username)
);
CREATE TABLE accounts (
	account_id varchar(50),
	user_id varchar(50) NOT NULL,
	balance varchar(4),
	CONSTRAINT PK_accounts PRIMARY KEY(account_id)
);
CREATE TABLE transfers (
	transfer_id varchar(50),
	transfer_type_id varchar(50) NOT NULL,
	transfer_status_id varchar(50) NOT NULL,
	account_from varchar(50) NOT NULL,
	account_to varchar(50) NOT NULL,
	amount varchar(50),
	CONSTRAINT PK_transfers PRIMARY KEY(transfer_id)

);

INSERT INTO users (user_id, username, password_hash)
VALUES ('1', 'A', 'password'),
       ('2', 'B', 'password');
      
INSERT INTO accounts (account_id, user_id, balance)
VALUES ('1L', '1L', 100),
       ('2L', '2L', 200);
     
INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES ('1L', '2L', '2L', '1L', '2L', 100),  
       ('2L', '2L', '2L', '2L', '1L', 200); 

COMMIT;               