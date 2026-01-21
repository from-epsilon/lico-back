CREATE TABLE public.user_details (
    user_id uuid NOT NULL,
    nickname character varying(255),
    core_value text,
    motive text,
    self_image text,
    verbosity_per_day double precision,
    sleep_time integer,
    wake_time integer,

    CONSTRAINT pk_user_details PRIMARY KEY (user_id),
    CONSTRAINT fk_user_details_user_id FOREIGN KEY (user_id) REFERENCES public.users(id)
        ON DELETE CASCADE
);

ALTER TABLE public.users
    DROP COLUMN IF EXISTS nickname;

ALTER TABLE public.users
    ALTER COLUMN last_login_at SET NOT NULL;
