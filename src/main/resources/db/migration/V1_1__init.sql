CREATE TABLE task (
  id text PRIMARY KEY,
  name varchar NOT NULL,
  status varchar NOT NULL,
  createdAt TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

