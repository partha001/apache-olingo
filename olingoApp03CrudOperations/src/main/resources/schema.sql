drop table accounts;
drop table customer;

create table customer
(
   id integer auto_increment not null,
   name varchar(255) not null,
   email varchar(255) ,
   primary key(id)
);



create table accounts(
	id integer auto_increment not null,
	customerid  integer,
    type varchar(255) not null,
    balance integer,
    foreign key (customerid) references customer(id),
    primary key(id)
);
