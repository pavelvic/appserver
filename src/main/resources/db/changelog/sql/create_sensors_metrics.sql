CREATE TABLE public.sensors_metrics (
    sensor_id bigint NOT NULL,
    metric_id bigint NOT NULL,
    CONSTRAINT sensors_metrics_fk_sensor FOREIGN KEY (sensor_id) REFERENCES public.sensors(id) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT sensors_metrics_fk_metric FOREIGN KEY (metric_id) REFERENCES public.metrics(id) ON DELETE CASCADE ON UPDATE RESTRICT
);