create table users (
    id text PRIMARY KEY,
    username VARCHAR(256) UNIQUE,
    password VARCHAR(256),
    enabled boolean
);

create table authorities (
  username  VARCHAR(256) REFERENCES users(username) ON DELETE CASCADE,
  authority VARCHAR(256),
  PRIMARY KEY (username, authority)
);