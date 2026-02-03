CREATE TABLE IF NOT EXISTS public.llm_jobs (
    id uuid NOT NULL,
    type varchar(32) NOT NULL,
    status varchar(16) NOT NULL,
    input_json jsonb NOT NULL,
    output_json jsonb,
    retry_count int NOT NULL DEFAULT 0,
    created_at timestamptz NOT NULL DEFAULT now(),
    completed_at timestamptz,
    applied_at timestamptz,

    CONSTRAINT pk_llm_jobs PRIMARY KEY (id),

    CONSTRAINT ck_llm_jobs_retry_count_non_negative
        CHECK (retry_count >= 0),
    CONSTRAINT ck_llm_jobs_type
        CHECK (type IN ('PUSH_SCHEDULE', 'USER_SUMMARY')),
    CONSTRAINT ck_llm_jobs_status
        CHECK (status IN ('PENDING', 'PROCESSING', 'SUCCESS', 'FAILED'))
);

CREATE INDEX IF NOT EXISTS idx_llm_jobs_pending
ON public.llm_jobs (id)
WHERE status = 'PENDING';

CREATE INDEX IF NOT EXISTS idx_llm_jobs_success_unapplied
ON public.llm_jobs (id)
WHERE status = 'SUCCESS' AND applied_at IS NULL;
