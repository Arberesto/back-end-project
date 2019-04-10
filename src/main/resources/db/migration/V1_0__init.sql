CREATE TABLE task (
  uuid bigserial PRIMARY KEY,
  name varchar NOT NULL,
  status varchar DEFAULT “inbox”
);

INSERT INTO task (name, status)
  VALUES ('First Task, “inbox”);