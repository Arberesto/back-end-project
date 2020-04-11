create table users (
  username VARCHAR(256) PRIMARY KEY,
  password VARCHAR(256),
  enabled boolean
);

create table authorities (
  username  VARCHAR(256) REFERENCES users(username) ON DELETE CASCADE,
  authority VARCHAR(256),
  PRIMARY KEY (username, authority)
);