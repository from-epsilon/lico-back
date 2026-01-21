CREATE TABLE public.fcm_tokens (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    token varchar(2048) NOT NULL,
    last_login_at timestamp(6) with time zone NOT NULL,
    CONSTRAINT pk_fcm_tokens PRIMARY KEY (id),
    CONSTRAINT uk_fcm_tokens_user_id UNIQUE (user_id),
    CONSTRAINT uk_fcm_tokens_token UNIQUE (token),
    CONSTRAINT fk_fcm_tokens_user_id FOREIGN KEY (user_id) REFERENCES public.users(id)
);
