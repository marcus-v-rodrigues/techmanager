CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  full_name VARCHAR(200) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone VARCHAR(30) NOT NULL,
  birth_date DATE NOT NULL,
  user_type VARCHAR(16) NOT NULL
);

ALTER TABLE users
  ADD CONSTRAINT users_user_type_check
  CHECK (user_type IN ('ADMIN', 'EDITOR', 'VIEWER'));
