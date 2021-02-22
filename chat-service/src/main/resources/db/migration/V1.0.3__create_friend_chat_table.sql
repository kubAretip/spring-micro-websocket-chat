create table if not exists friend_chat
(
    id           bigint     not null primary key auto_increment,
    chat_with_id bigint,
    sender_id    binary(16) not null,
    recipient_id binary(16) not null,
    foreign key (chat_with_id) references friend_chat (id),
    foreign key (sender_id) references chat_profile (user_id),
    foreign key (recipient_id) references chat_profile (user_id),
    unique index (chat_with_id, sender_id, recipient_id)
);