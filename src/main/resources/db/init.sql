CREATE TABLE IF NOT EXISTS items (
   id int PRIMARY KEY auto_increment,
   name VARCHAR,
   url VARCHAR
);

CREATE TABLE IF NOT EXISTS reviews (
   id INTEGER PRIMARY KEY auto_increment,
   item_id INTEGER,
   rating INTEGER,
   comment VARCHAR,
   FOREIGN KEY(item_id) REFERENCES public.items(id)
);