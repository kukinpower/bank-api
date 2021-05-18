create table IF NOT EXISTS card
(number char(16) NOT NULL primary key,
    pin char(4) NOT NULL,
    account INTEGER NOT NULL,
    currency char(3) NOT NULL,
    balance DECIMAL NOT NULL,
    status INTEGER NOT NULL);

CREATE TABLE IF NOT EXISTS `status` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `descriptor` VARCHAR(255) NOT NULL UNIQUE,
      PRIMARY KEY (`id`)
);


-- insert into card values('1234567890123456', '1234', 4, 'RUB', 50000.00, 1);
-- insert into card values('2345612345678901', '4444', 3, 'RUB', 450000.00, 1);