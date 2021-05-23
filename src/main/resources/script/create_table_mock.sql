CREATE TABLE IF NOT EXISTS status (
    id INT NOT NULL,
    descriptor VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id));

merge into status key(id) values(1, 'PENDING');
merge into status key(id) values(2, 'ACTIVE');
merge into status key(id) values(3, 'CLOSED');

create table IF NOT EXISTS card
    (number char(16) NOT NULL primary key,
    pin char(4) NOT NULL,
    account char(20) NOT NULL,
    currency char(3) NOT NULL,
    balance DECIMAL NOT NULL,
    status INTEGER NOT NULL,
    FOREIGN KEY (status) REFERENCES status(id));

insert into card values('4000006080001109', '9989', 12345123451234512345, 'RUB', 50000.00, 2);
insert into card values('4000000967827322', '4957', 12345123451234512345, 'RUB', 150.50, 2);
insert into card values('4000005726498919', '1408', 12345123451234512345, 'RUB', 0, 3);
insert into card values('4000005231198475', '2457', 51111511115111151111, 'RUB', 0, 1);
insert into card values('4000006804033693', '1345', 21111211112111121111, 'RUB', 0, 1);
insert into card values('4000007827033868', '1139', 31111311113111131111, 'RUB', 0, 1);
insert into card values('4000004571668296', '2183', 41111411114111141111, 'RUB', 0, 1);
