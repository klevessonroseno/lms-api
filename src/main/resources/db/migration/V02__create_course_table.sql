--database: ../../../../../database.sqlite

CREATE TABLE courses (
    id INTEGER PRIMARY KEY,
    slug TEXT NOT NULL COLLATE NOCASE UNIQUE,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    lessons INTEGER NOT NULL,
    hours INTEGER NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
) STRICT;

UPDATE courses SET
    slug = " ",
    title = "",
    description = "",
    lessons = 20,
    hours = 10,
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;