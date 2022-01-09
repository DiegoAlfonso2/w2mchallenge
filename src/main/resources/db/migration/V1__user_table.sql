CREATE TABLE User (
	username				VARCHAR(255)	PRIMARY KEY,
	hashed_password			CHAR(64)		NOT NULL,
	roles					VARCHAR(255)
);