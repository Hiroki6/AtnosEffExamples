CREATE TABLE users (
    id varchar(36) NOT NULL,
    name varchar(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE posts(
    id varchar(36) NOT NULL,
    user_id varchar(36) NOT NULL,
    text text,
    posted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id),
    PRIMARY KEY (id)
);

CREATE TABLE post_rel(
  parent_id varchar(36),
  child_id varchar(36),
  FOREIGN KEY(parent_id) REFERENCES posts(id),
  FOREIGN KEY(child_id) REFERENCES posts(id),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);