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

insert into client(first_name, last_name, phone) values('Fyodor', 'Dostoevsky', '+79504332233');
insert into client(first_name, last_name, phone) values('Leo', 'Tolstoy', '+75557774433');
insert into client(first_name, last_name, phone) values('Mikhail', 'Bulgakov', '+75557774432');
insert into client(first_name, last_name, phone) values('Ivan', 'Turgenev', '+75557774431');
insert into client(first_name, last_name, phone) values('Nikolay', 'Karamzin', '+75557774430');

insert into account(number, clientId) values('12345123451234512345', 1);
insert into account(number, clientId) values('51111511115111151111', 2);
insert into account(number, clientId) values('21111211112111121111', 3);
insert into account(number, clientId) values('31111311113111131111', 4);
insert into account(number, clientId) values('41111411114111141111', 5);

insert into card(number, pin, accountId, currency, balance, status) values('4000006080001109', '9989', 1, 'RUB', 50000.00, 2);
insert into card(number, pin, accountId, currency, balance, status) values('4000000967827322', '4957', 1, 'RUB', 150.50, 2);
insert into card(number, pin, accountId, currency, balance, status) values('4000005726498919', '1408', 1, 'RUB', 0, 3);
insert into card(number, pin, accountId, currency, balance, status) values('4000005231198475', '2457', 2, 'RUB', 0, 1);
insert into card(number, pin, accountId, currency, balance, status) values('4000006804033693', '1345', 3, 'RUB', 35000, 1);
insert into card(number, pin, accountId, currency, balance, status) values('4000007827033868', '1139', 4, 'RUB', 110, 1);
insert into card(number, pin, accountId, currency, balance, status) values('4000004571668296', '2183', 5, 'RUB', 90, 1);
