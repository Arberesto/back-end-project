insert into users (id,username, password, enabled)
    values ('f67f5506-9fb7-4536-a932-5c409cbe9151',''admin', '$2a$10$NTKkyxbcddQT7JAfjtRRuuR3TvYMdm5bOOFofFCsWSeT/ZqDPvqWi', true);
insert into authorities (username, authority)
    values ('admin', 'USER');
insert into authorities (username, authority)
    values ('admin', 'ADMIN');

insert into users (id, username, password, enabled)
    values ('8b43e73d-2876-4a61-a0f0-607661cde137',''user', '$2a$10$AiSUhtNeGjDxWkVJLIVvzeEXxX47miExIiPzq4aivgpSJMj1vLgLu', true);
insert into authorities (username, authority)
    values ('user', 'USER');