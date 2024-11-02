create table wetalk_user
(
    id            bigint primary key not null,
    wechat_id          varchar,
    user_name          varchar,
    icon               varchar,
    create_at      timestamp(6) default current_timestamp
);

create table wetalk_site
(
    id            bigint primary key not null,
    site_name         varchar,
    email          varchar,
    city               varchar,
    logo               varchar,
    create_at      timestamp(6) default current_timestamp
);

create table wetalk_user_site
(
    id            bigint primary key not null,
    user_id             bigint,
    user_name             varchar,
    invite_by             bigint,
    site_id             bigint,
    status                varchar,
    as_runner           boolean,
    deleted      boolean default false,
    create_at      timestamp(6) default current_timestamp
);

create table wetalk_activity
(
    id            bigint primary key not null,
    title             varchar,
    start_at             date,
    detail             varchar,
    audiences           int,
    user_id             bigint,
    site_id             bigint,
    site_name             varchar,
    deleted      boolean default false,
    create_at      timestamp(6) default current_timestamp
);

create table wetalk_user_activity
(
    id            bigint primary key not null,
    activity_id              bigint,
    user_id           bigint,
    user_name             varchar,
    email             varchar,
    role          varchar,
    role_text          varchar,
    create_at      timestamp(6) default current_timestamp
);

