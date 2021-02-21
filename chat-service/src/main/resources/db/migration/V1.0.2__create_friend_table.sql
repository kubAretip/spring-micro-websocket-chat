create table if not exists friend
(
    id                        bigint      not null auto_increment primary key,
    sender_chat_profile_id    binary(16)  not null,
    recipient_chat_profile_id binary(16)  not null,
    friends_request_status    varchar(50) not null,
    sent_time                 datetime(6) not null,
    foreign key (sender_chat_profile_id) references chat_profile (user_id),
    foreign key (recipient_chat_profile_id) references chat_profile (user_id),
    unique index (sender_chat_profile_id, recipient_chat_profile_id)
)