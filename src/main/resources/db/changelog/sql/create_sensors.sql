CREATE TABLE public.sensors (
	id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
	name varchar NOT NULL,
	description varchar NULL,
	CONSTRAINT sensors_pk PRIMARY KEY (id)
);