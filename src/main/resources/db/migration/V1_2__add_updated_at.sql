ALTER TABLE task
    ADD COLUMN  updatedAt TIMESTAMPTZ NOT NULL DEFAULT NOW();
