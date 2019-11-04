delete from accounts;
delete from customer;

insert into customer values(10002,'Sachin Tendulkar','sachin@gmail.com');
insert into customer values(10003,'Sourav Ganguly','sourav@gmail.com');


insert into accounts values(5000,10002,'savings',10000);
insert into accounts values(5001,10002,'recurring',20000);
insert into accounts values(5002,10003,'savings',50000);
