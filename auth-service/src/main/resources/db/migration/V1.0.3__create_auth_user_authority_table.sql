create table if not exists auth_user_authority
(
    auth_user_id   bigint      not null,
    authority_name varchar(50) not null,
    primary key (auth_user_id, authority_name),
    foreign key (auth_user_id) references auth_user (id),
    foreign key (authority_name) references authority (name)
);