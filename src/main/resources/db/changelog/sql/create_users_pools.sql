CREATE TABLE public.users_pools (
	user_id bigint NULL,
	pool_id bigint NOT NULL,
	CONSTRAINT users_pools_un UNIQUE (user_id,pool_id),
	CONSTRAINT users_pools_fk_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT users_pools_fk_pool FOREIGN KEY (pool_id) REFERENCES public.pools(id) ON DELETE CASCADE ON UPDATE RESTRICT
);