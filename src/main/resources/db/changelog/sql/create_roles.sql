CREATE TABLE public.roles
(id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 name VARCHAR(20) NOT NULL,
 CONSTRAINT roles_pkey PRIMARY KEY (id), UNIQUE (name))