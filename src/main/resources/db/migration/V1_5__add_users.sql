insert into users (username, password, enabled)
    values ('admin', '$2a$10$NTKkyxbcddQT7JAfjtRRuuR3TvYMdm5bOOFofFCsWSeT/ZqDPvqWi', true);
insert into authorities (username, authority)
    values ('admin', 'USER');
insert into authorities (username, authority)
    values ('admin', 'ADMIN');

insert into users (username, password, enabled)
    values ('user', '$2a$10$AiSUhtNeGjDxWkVJLIVvzeEXxX47miExIiPzq4aivgpSJMj1vLgLu', true);
insert into authorities (username, authority)
    values ('user', 'USER');