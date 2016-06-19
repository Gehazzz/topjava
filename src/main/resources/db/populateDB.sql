DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);


DELETE FROM users_meals;
DELETE FROM meals;
ALTER SEQUENCE global_seq_meal RESTART WITH 100000;

INSERT INTO meals (date_time, description, calories)
VALUES ('2016-06-18 13:33:37.5168', 'dinner', 1000);

INSERT INTO meals (date_time, description, calories)
VALUES ('2016-06-18 19:20:00.6', 'supper', 1001);

INSERT INTO meals (date_time, description, calories)
VALUES ('2016-06-17 08:20:10.6', 'breakfast', 400);

/*INSERT INTO meals (date_time, description, calories)
VALUES ('2016-06-17 14:45:04.56', 'obed', 1200);

INSERT INTO meals (date_time, description, calories)
VALUES ('2016-06-17 18:47:14.74', 'ujin', 750);*/

INSERT INTO users_meals (meal_id, user_id) VALUES
  (100000, 100001),
  (100001, 100001),
  (100002, 100000);
 /* (100003, 100000),
  (100004, 100000);*/
