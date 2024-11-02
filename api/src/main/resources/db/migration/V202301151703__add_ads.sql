create table wetalk_ads
(
    id              bigint primary key not null,
    content         varchar,
    url             varchar,
    status          varchar,
    create_at       timestamp(6) default current_timestamp
);