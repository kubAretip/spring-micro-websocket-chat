create table if not exists friend_request
(
    id                        bigint      not null auto_increment primary key,
    sender_chat_profile_id    binary(16)  not null,
    recipient_chat_profile_id binary(16)  not null,
    sent_time                 datetime(6) not null,
    is_accepted               boolean     not null default 0,
    foreign key (sender_chat_profile_id) references chat_profile (user_id),
    foreign key (recipient_chat_profile_id) references chat_profile (user_id),
    unique index (sender_chat_profile_id, recipient_chat_profile_id)
)