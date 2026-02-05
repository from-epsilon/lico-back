ALTER TABLE public.server_messages
    ALTER COLUMN data_json DROP NOT NULL;

ALTER TABLE public.server_messages
    ALTER COLUMN llm_meta_json DROP NOT NULL;
