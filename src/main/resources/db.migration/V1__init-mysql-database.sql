drop table if exists baby;
drop table if exists event;
drop table if exists userAccount;

create table baby
(
    id                 varchar(36) not null,
    created_date       datetime(6),
    last_modified_date datetime(6),
    name               varchar(50) not null,
    version            integer,
    primary key (id)
) engine=InnoDB;

create table event
(
    id                 varchar(36) not null,
    created_date       datetime(6),
    end_date           datetime(6),
    event_type         smallint    not null,
    last_modified_date datetime(6),
    notes              varchar(255),
    start_date         datetime(6),
    version            integer,
    primary key (id)
) engine=InnoDB;

create table userAccount
(
    id                 varchar(36) not null,
    created_date       datetime(6),
    email              varchar(255),
    last_modified_date datetime(6),
    password           varchar(50) not null,
    username           varchar(50) not null,
    version            integer,
    primary key (id)
) engine=InnoDB;
