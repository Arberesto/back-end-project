CREATE TABLE task (
  id text PRIMARY KEY,
  name varchar NOT NULL,
  status varchar NOT NULL,
  createdAt TIMESTAMP NOT NULL DEFAULT NOW()
);

