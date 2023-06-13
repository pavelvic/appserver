INSERT INTO public.users (email,username,"password")
	VALUES ('adm@adm.com','admin','$2a$04$uxStcwd9ly42.C5Wr.BOa.AAwa1xZMVBZ3wS/jVSxRJBsEXjPMPr2');
INSERT INTO public.users (email,username,"password")
	VALUES ('mod@mod.com','mod','$2a$04$rVDgnLFGdLebJrdmTvbEYutDjgHXvAqDcMadwWuyOAc72mORAqkFe');
INSERT INTO public.users (email,username,"password")
	VALUES ('user@user.com','user','$2a$04$MaiLpYMMoY5XiFf92wX4gujnSlCxRannonis4WNbyr8zcJqGFQZPW');
INSERT INTO public.user_roles (user_id,role_id)
	VALUES (1,3);
INSERT INTO public.user_roles (user_id,role_id)
	VALUES (1,2);
INSERT INTO public.user_roles (user_id,role_id)
	VALUES (1,1);
INSERT INTO public.user_roles (user_id,role_id)
	VALUES (2,2);
INSERT INTO public.user_roles (user_id,role_id)
	VALUES (2,1);
INSERT INTO public.user_roles (user_id,role_id)
	VALUES (3,1);