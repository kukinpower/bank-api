insert into client(first_name, last_name, phone) values('Fyodor', 'Dostoevsky', '+79504332233');
insert into client(first_name, last_name, phone) values('Leo', 'Tolstoy', '+75557774433');
insert into client(first_name, last_name, phone) values('Mikhail', 'Bulgakov', '+75557774432');
insert into client(first_name, last_name, phone) values('Ivan', 'Turgenev', '+75557774431');
insert into client(first_name, last_name, phone) values('Nikolay', 'Karamzin', '+75557774430');

insert into account(number, balance, clientId) values('12345123451234512345', 50150.00, 1);
insert into account(number, balance, clientId) values('51111511115111151111', 0, 2);
insert into account(number, balance, clientId) values('21111211112111121111', 35000, 3);
insert into account(number, balance, clientId) values('31111311113111131111', 110, 4);
insert into account(number, balance, clientId) values('41111411114111141111', 90, 5);

insert into card(number, pin, accountId, currency, balance, status) values('4000006080001109', '9989', 1, 'RUB', 50000.00, 2);
insert into card(number, pin, accountId, currency, balance, status) values('4000000967827322', '4957', 1, 'RUB', 150.50, 2);
insert into card(number, pin, accountId, currency, balance, status) values('4000005726498919', '1408', 1, 'RUB', 0, 3);
insert into card(number, pin, accountId, currency, balance, status) values('4000005231198475', '2457', 2, 'RUB', 0, 1);
insert into card(number, pin, accountId, currency, balance, status) values('4000006804033693', '1345', 3, 'RUB', 35000, 1);
insert into card(number, pin, accountId, currency, balance, status) values('4000007827033868', '1139', 4, 'RUB', 110, 1);
insert into card(number, pin, accountId, currency, balance, status) values('4000004571668296', '2183', 5, 'RUB', 90, 1);
