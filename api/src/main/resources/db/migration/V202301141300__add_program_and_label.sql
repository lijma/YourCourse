create table wetalk_program
(
    id            bigint primary key not null,
    title             varchar,
    start_at            date,
    end_at            date,
    stop_at             date,
    price             bigint,
    user_id             bigint,
    img               varchar,
    detail            varchar,
    deleted          boolean default false,
    create_at      timestamp(6) default current_timestamp
);

create table wetalk_label
(
    id            bigint primary key not null,
    label_type          varchar,
    ref_id          bigint,
    label            varchar,
    create_at      timestamp(6) default current_timestamp
);

