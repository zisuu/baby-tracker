alter table baby
    drop
        foreign key FKd2pr45e345wjhv4h6fg6r054i;

alter table event
    drop
        foreign key FKfso3pt1na1qo4mtdov8331e7r;

drop table if exists baby;

drop table if exists event;

drop table if exists user_account;

create table baby
(
    id                 varchar(36) not null,
    created_date       datetime(6),
    last_modified_date datetime(6),
    name               varchar(50) not null,
    birthday           DATE,
    version            integer,
    user_account_id    varchar(36),
    primary key (id)
) engine = InnoDB;

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
    baby_id            varchar(36),
    primary key (id)
) engine = InnoDB;

create table user_account
(
    id                 varchar(36) not null,
    created_date       datetime(6),
    email              varchar(255),
    last_modified_date datetime(6),
    password           varchar(50) not null,
    username           varchar(50) not null,
    version            integer,
    primary key (id)
) engine = InnoDB;

alter table baby
    add constraint FKd2pr45e345wjhv4h6fg6r054i
        foreign key (user_account_id)
            references user_account (id);

alter table event
    add constraint FKfso3pt1na1qo4mtdov8331e7r
        foreign key (baby_id)
            references baby (id);
