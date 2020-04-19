insert into users (username, password, enabled)
    values ('admin', '$2a$10$h.NGUnBdsXFkdcsZ8p.ZFeuAO4U73j5x83nBZHKjIqC82l6LV/S3u', true);
insert into authorities (username, authority)
    values ('admin', 'USER');
insert into authorities (username, authority)
    values ('admin', 'ADMIN');

insert into users (username, password, enabled)
    values ('user', '$2a$10$84nwdGu.BZhS68pkgJAWxuDVgcvKz5OGKnxvHgZjzijXzke.LTuci', true);
insert into authorities (username, authority)
    values ('user', 'USER');