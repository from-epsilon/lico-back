ALTER TABLE public.llm_jobs
    DROP CONSTRAINT IF EXISTS ck_llm_jobs_type;

UPDATE public.llm_jobs
SET type = 'REMINDER'
WHERE type = 'PUSH_BATCH';

ALTER TABLE public.llm_jobs
    ADD CONSTRAINT ck_llm_jobs_type
        CHECK (type IN ('REMINDER', 'ADDITIONAL', 'USER_SUMMARY', 'COMPACTION'));

ALTER TABLE public.user_messages
    ALTER COLUMN server_message_id SET NOT NULL;

CREATE TABLE IF NOT EXISTS public.plan_logs (
    plan_id uuid NOT NULL,
    user_id uuid NOT NULL,
    compaction text,
    recent_logs jsonb[] NOT NULL DEFAULT '{}',
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),

    CONSTRAINT pk_plan_logs PRIMARY KEY (plan_id),

    CONSTRAINT fk_plan_logs_plan_id
        FOREIGN KEY (plan_id)
        REFERENCES public.plans(id),

    CONSTRAINT fk_plan_logs_user_id
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
);

CREATE INDEX IF NOT EXISTS idx_plan_logs_user_id
    ON public.plan_logs(user_id);
