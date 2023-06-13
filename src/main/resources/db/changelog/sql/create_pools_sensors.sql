CREATE TABLE public.pools_sensors (
	pool_id bigint NOT NULL,
	sensor_id bigint NOT NULL,
	CONSTRAINT pools_sensors_fk_pool FOREIGN KEY (pool_id) REFERENCES public.pools(id) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT pools_sensors_fk_sensor FOREIGN KEY (sensor_id) REFERENCES public.sensors(id) ON DELETE CASCADE ON UPDATE RESTRICT
);