-- noinspection SqlNoDataSourceInspectionForFile

create table if not exists talks(
    id varchar(32) not null,
    title varchar(255) not null,
    primary key (id)
);

insert
    into talks (id, title)
    values ('welcome-to-junit-5', 'JUnit 5.2 actually :)')
    on conflict do nothing;

insert
    into talks (id, title)
    values ('flight-of-the-flux', 'A look at Reactor execution model')
    on conflict do nothing;
