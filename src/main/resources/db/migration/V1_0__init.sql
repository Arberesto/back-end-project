CREATE TABLE task (
  id text PRIMARY KEY,
  name varchar NOT NULL,
  status varchar NOT NULL
);

INSERT INTO task (id, name, status)
  VALUES ('60d0a438-71df-4852-899c-573c5108e607', 'First Task', 'inbox');