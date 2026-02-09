ALTER TABLE public.server_messages
    ALTER COLUMN plan_id DROP NOT NULL;

DROP TABLE IF EXISTS public.user_messages;

CREATE TABLE public.user_messages (
    id uuid NOT NULL,
    server_message_id bigint,
    user_id uuid NOT NULL,
    plan_id uuid NOT NULL,
    body text NOT NULL,
    sent_at timestamptz NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),

    CONSTRAINT user_messages_pkey PRIMARY KEY (id),

    CONSTRAINT fk_user_messages_server_message_id
        FOREIGN KEY (server_message_id)
        REFERENCES public.server_messages(id),

    CONSTRAINT fk_user_messages_user_id
        FOREIGN KEY (user_id)
        REFERENCES public.users(id),

    CONSTRAINT fk_user_messages_plan_id
        FOREIGN KEY (plan_id)
        REFERENCES public.plans(id)
);

CREATE INDEX idx_user_messages_user_id
    ON public.user_messages(user_id);

CREATE INDEX idx_user_messages_plan_id
    ON public.user_messages(plan_id);

CREATE INDEX idx_user_messages_server_message_id
    ON public.user_messages(server_message_id);
