create sequence channel_seq start with 1 increment by 50;
create sequence message_seq start with 1 increment by 50;
create sequence user_profile_seq start with 1 increment by 50;
create table channel
(
    channel_id   bigint                      not null,
    created      timestamp(6) with time zone not null,
    external_key uuid                        not null unique,
    title        varchar(30)                 not null unique,
    primary key (channel_id)
);
create table message
(
    author_id    bigint                      not null,
    channel_id   bigint                      not null,
    message_id   bigint                      not null,
    posted       timestamp(6) with time zone not null,
    external_key uuid                        not null unique,
    text         varchar(255)                not null,
    primary key (message_id)
);
create table user_profile
(
    created         timestamp(6) with time zone not null,
    user_profile_id bigint                      not null,
    external_key    uuid                        not null unique,
    display_name    varchar(30)                 not null unique,
    oauth_key       varchar(30)                 not null unique,
    avatar          varchar(255),
    primary key (user_profile_id)
);
alter table if exists message
    add constraint FKpmcktsmn59ssheglf3wtly2bq foreign key (author_id) references user_profile;
alter table if exists message
    add constraint FKiimr93ytmcuira5le0sldvvma foreign key (channel_id) references channel;
