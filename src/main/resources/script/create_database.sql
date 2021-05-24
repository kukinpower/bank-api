CREATE TABLE IF NOT EXISTS status (
    id INT NOT NULL,
    descriptor VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id));

merge into status key(id) values(1, 'PENDING');
merge into status key(id) values(2, 'ACTIVE');
merge into status key(id) values(3, 'CLOSED');

create table IF NOT EXISTS client
(id int NOT NULL auto_increment,
 first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    phone varchar(20) NOT NULL unique,
    primary key(id));

create table IF NOT EXISTS account
(id int not null auto_increment,
 number char(20) NOT NULL unique,
    balance decimal NOT NULL,
    clientId int NOT NULL,
    primary key(id),
    FOREIGN KEY (clientId) REFERENCES client(id));

create table IF NOT EXISTS card
(id int NOT NULL auto_increment,
 number char(16) NOT NULL unique,
    pin char(4) NOT NULL,
    accountId int NOT NULL,
    currency char(3) NOT NULL,
    balance DECIMAL NOT NULL,
    status INTEGER NOT NULL,
    primary key(id),
    FOREIGN KEY (status) REFERENCES status(id),
    FOREIGN KEY (accountId) REFERENCES account(id));
