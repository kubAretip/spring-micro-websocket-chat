create table if not exists user
(
    id                  bigint       not null primary key auto_increment,
    username            varchar(50)  not null unique,
    first_name          varchar(50)  not null,
    last_name           varchar(50)  not null,
    email               varchar(254) not null unique,
    friend_request_code varchar(64)  not null unique
)