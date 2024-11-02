create table wetalk_vote
(
    id            bigint primary key not null,
    activity_id        bigint,
    user_id           bigint,
    question         varchar,
    answer             varchar,
    create_at      timestamp(6) default current_timestamp
);