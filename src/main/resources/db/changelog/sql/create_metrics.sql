CREATE TABLE public.metrics (
	id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
	name varchar NOT NULL,
	description varchar NULL,
	critical_min_value bigint NULL,
	critical_max_value bigint NULL,
	CONSTRAINT metrics_pk PRIMARY KEY (id)
);