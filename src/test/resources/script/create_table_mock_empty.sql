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