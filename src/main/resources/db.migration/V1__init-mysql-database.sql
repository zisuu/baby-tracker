drop table if exists baby cascade;
drop table if exists event cascade;
create table baby
(
    id                 varchar(36) not null,
    created_date       timestamp(6),
    last_modified_date timestamp(6),
    name               varchar(50) not null,
    version            integer,
    primary key (id)
);
create table event
(
    id                 varchar(36) not null,
    created_date       timestamp(6),
    end_date           timestamp(6),
    event_type         smallint    not null,
    last_modified_date timestamp(6),
    notes              varchar(255),
    start_date         timestamp(6),
    version            integer,
    primary key (id)
);
