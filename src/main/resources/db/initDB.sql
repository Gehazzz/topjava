DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 100000;

CREATE TABLE users
(
  id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name       VARCHAR NOT NULL,
  email      VARCHAR NOT NULL,
  password   VARCHAR NOT NULL,
  registered TIMESTAMP DEFAULT now(),
  enabled    BOOL DEFAULT TRUE,
  calories_per_day INTEGER DEFAULT 2000 NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- DROP TABLE IF EXISTS users_meals;
DROP TABLE IF EXISTS meals;
DROP SEQUENCE IF EXISTS global_seq_meal;

CREATE SEQUENCE global_seq_meal START 100000;

/*CREATE TABLE meals
(
  id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq_meal'),
  date_time   TIMESTAMP DEFAULT now(),
  description VARCHAR NOT NULL,
  calories    INTEGER DEFAULT 0 NOT NULL
);
CREATE UNIQUE INDEX meals_unique_id_idx ON meals (id);

CREATE TABLE users_meals
(
  meal_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  CONSTRAINT user_meals_idx UNIQUE (meal_id, user_id),
  FOREIGN KEY (meal_id) REFERENCES meals (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);*/

CREATE TABLE meals
(
  id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq_meal'),
  date_time   TIMESTAMP DEFAULT now(),
  description VARCHAR NOT NULL,
  calories    INTEGER DEFAULT 0 NOT NULL,
  user_id     INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX meals_unique_id_idx ON meals (id);
CREATE UNIQUE INDEX meals_unique_user_id_idx ON meals (user_id);