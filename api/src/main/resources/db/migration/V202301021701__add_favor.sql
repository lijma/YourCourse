create table wetalk_favor
(
    id            bigint primary key not null,
    user_id          bigint,
    site_id          bigint,
    favor            boolean,
    favor_type       varchar,
    create_at      timestamp(6) default current_timestamp
);