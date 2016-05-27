drop table share if exists;
drop table request if exists;
drop table record if exists;
drop table expensive_request if exists;
drop table user_role if exists;
drop table deposit_req if exists;
drop table stock if exists;
drop table pending_stock if exists;
drop table config if exists;
drop table user if exists;


create table user (
    id integer not null,
    password varchar(100) not null,
    name varchar(100) not null,
    family varchar(100) not null,
    email varchar(100) not null,
    balance integer not null,
    primary key (id)
);

create table user_role (
    id  integer not null,
    role varchar(20) not null,
    foreign key(id) references user(id),
    primary key (id,role)
);

create table stock (
    id varchar(20) not null,
    owner_id integer not null,
    foreign key(owner_id) references user(id),    
    primary key (id)
);

create table pending_stock (
    id varchar(20) not null,
    owner_id integer not null,
    quantity integer not null,
    price integer not null,
    request_type varchar(4) not null,
    foreign key(owner_id) references user(id),
    primary key (id)
);

create table share (
    stock_id varchar(20) not null,
    user_id  integer not null,
    quantity integer not null,
    primary key (stock_id,user_id),    
    foreign key(stock_id) references stock(id),
    foreign key(user_id) references user(id)
);

create table request (
    id integer not null,
    stock_id varchar(20) not null,
    user_id integer not null,
    quantity integer not null,
    price integer not null,
    request_type varchar(4) not null,
    op_type varchar(3) not null,
    primary key (id),
    foreign key(stock_id) references stock(id),
    foreign key(user_id) references user(id)
);

create table expensive_request (
    id integer not null,
    stock_id varchar(20) not null,
    user_id integer not null,
    quantity integer not null,
    price integer not null,
    request_type varchar(4) not null,
    op_type varchar(3) not null,
    primary key (id),
    foreign key(stock_id) references stock(id),
    foreign key(user_id) references user(id)
);

create table record (
    id integer not null,
    stock_id varchar(20) not null,
    seller_id integer not null,
    buyer_id integer not null,
    quantity integer not null,
    price integer not null,
    seller_balance integer not null,
    buyer_balance integer not null,
    op_type varchar(3) not null,
    reg_date timestamp not null,
    primary key (id),
    foreign key(stock_id) references stock(id),
    foreign key(seller_id) references user(id),
    foreign key(buyer_id) references user(id)
);

create table deposit_req (
    id integer not null,
    user_id integer not null,
    amount integer not null,
    foreign key(user_id) references user(id),
    primary key (id)
);


create table config (
    name varchar(20) not null,
    value varchar(20) not null,
    primary key (name)
);

insert into user values (1,'admin','admin','adminy','damaroo.con',0);
insert into user_role values (1,'admin');

