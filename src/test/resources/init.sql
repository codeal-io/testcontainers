
create table if not exists book(
    id SERIAL primary key,
    title varchar(100),
    author varchar(255),
    year int
)