create table wetalk_config
(
    id           bigint primary key not null,
    config_type  varchar,
    config_value varchar,
    create_at    timestamp(6) default current_timestamp
);