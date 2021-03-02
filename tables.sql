create table countries
(
    id   bigserial not null
        constraint countries_pk
            primary key,
    name varchar(50)
);

create table transport
(
    id   bigserial   not null
        constraint transport_pk
            primary key,
    name varchar(25) not null
);
create table agencies
(
    id      bigserial    not null
        constraint agencies_pk
            primary key,
    name    varchar(50),
    phone   varchar(13),
    address varchar(100) not null,
    years   integer
);

create table agencies_to_transport
(
    agency_id    bigserial not null
        constraint agencies_to_transport_agencies_id_fk
            references agencies,
    transport_id bigserial not null
        constraint agencies_to_transport_transport_id_fk
            references transport
);

create table tours
(
    id        bigserial        not null
        constraint tours_pk
            primary key,
    name      varchar(100)     not null,
    amount    double precision not null,
    duration  integer          not null,
    departure timestamp        not null,
    agency_id bigserial        not null
        constraint tours_tours_id_fk
            references tours
);

create table tours_to_countries
(
    tour_id    bigserial not null
        constraint tours_to_countries_countries_id_fk
            references tours,
    country_id bigserial not null
        constraint tours_to_countries_countries_id_fk_2
            references countries
);

