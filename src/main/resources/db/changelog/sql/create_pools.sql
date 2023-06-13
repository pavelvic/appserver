CREATE TABLE public.pools (
	id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
	name varchar NOT NULL,
	address varchar NULL,
	CONSTRAINT pool_pk PRIMARY KEY (id)
);