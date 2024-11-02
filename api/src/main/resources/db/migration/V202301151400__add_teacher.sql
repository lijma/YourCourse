create table wetalk_teacher
(
    id              bigint primary key not null,
    teacher_name    varchar,
    email           varchar,
    city            varchar,
    logo            varchar,
    detail             varchar,
    url             varchar,
    status          varchar,
    comment         varchar,
    user_id         bigint,
    create_at       timestamp(6) default current_timestamp
);