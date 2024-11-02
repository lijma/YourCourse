create table wetalk_user_program
(
    id            bigint primary key not null,
    program_id        bigint,
    user_id           bigint,
    user_name         varchar,
    email             varchar,
    create_at      timestamp(6) default current_timestamp
);