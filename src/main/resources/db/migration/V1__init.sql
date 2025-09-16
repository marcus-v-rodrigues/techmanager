CREATE TYPE user_type AS ENUM ('ADMIN', 'EDITOR', 'VIEWER');

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  full_name VARCHAR(200) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone VARCHAR(30) NOT NULL,
  birth_date DATE NOT NULL,
  user_type user_type NOT NULL
);