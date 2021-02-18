create table if not exists auth_user
(
    id             bigint      not null auto_increment primary key,
    username       varchar(50) not null unique,
    password_hash  varchar(60) not null,
    activation_key varchar(124) unique,
    enabled        boolean     not null default 0
);