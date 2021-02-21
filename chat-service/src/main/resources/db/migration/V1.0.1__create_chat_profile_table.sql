create table if not exists chat_profile
(
    user_id              BINARY(16)  not null unique primary key,
    friends_request_code varchar(64) not null unique
)