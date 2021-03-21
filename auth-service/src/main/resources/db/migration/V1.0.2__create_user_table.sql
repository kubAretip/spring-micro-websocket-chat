create table if not exists auth_service_database.user
(
    id             binary(16)   not null primary key,
    username       varchar(50)  not null unique,
    password_hash  varchar(60)  not null,
    first_name     varchar(50)  not null,
    last_name      varchar(50)  not null,
    activation_key varchar(124) unique,
    email          varchar(100) not null unique,
    enabled        boolean      not null default 0
);