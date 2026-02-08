ALTER TABLE public.chat_messages
    RENAME TO user_messages;

ALTER TABLE public.user_messages
    RENAME CONSTRAINT chat_messages_pkey TO user_messages_pkey;

ALTER TABLE public.user_messages
    RENAME CONSTRAINT fk_chat_messages_plan_id TO fk_user_messages_plan_id;

ALTER TABLE public.user_messages
    RENAME CONSTRAINT fk_chat_messages_snapshot_id TO fk_user_messages_snapshot_id;

ALTER TABLE public.user_messages
    RENAME CONSTRAINT chat_messages_type_check TO user_messages_type_check;

ALTER INDEX IF EXISTS public.idx_chat_messages_plan_id_created_at
    RENAME TO idx_user_messages_plan_id_created_at;

ALTER TABLE public.user_messages
    ADD COLUMN IF NOT EXISTS client_message_id uuid;

ALTER TABLE public.user_messages
    ADD COLUMN IF NOT EXISTS server_message_id bigint;

ALTER TABLE public.user_messages
    ADD COLUMN IF NOT EXISTS sent_at timestamptz;

ALTER TABLE public.user_messages
    ADD CONSTRAINT fk_user_messages_server_message_id
        FOREIGN KEY (server_message_id)
        REFERENCES public.server_messages(id);

CREATE UNIQUE INDEX IF NOT EXISTS uk_user_messages_client_message_id
    ON public.user_messages(client_message_id);
