ALTER TABLE public.llm_jobs
    DROP CONSTRAINT IF EXISTS ck_llm_jobs_type;

ALTER TABLE public.llm_jobs
    ADD CONSTRAINT ck_llm_jobs_type
        CHECK (type IN ('PUSH_BATCH', 'USER_SUMMARY'));

ALTER TABLE public.push_jobs
    RENAME COLUMN kind TO type;

UPDATE public.push_jobs
SET type = 'REMINDER'
WHERE type = 'REMIND';

UPDATE public.push_jobs
SET type = 'ADDITIONAL'
WHERE type = 'GENERAL';

ALTER TABLE public.push_jobs
    DROP CONSTRAINT IF EXISTS ck_push_jobs_kind;

ALTER TABLE public.push_jobs
    ADD CONSTRAINT ck_push_jobs_type
        CHECK (type IN ('REMINDER', 'ADDITIONAL'));

ALTER TABLE public.push_jobs
    DROP CONSTRAINT IF EXISTS fk_push_jobs_batch;

DROP TABLE IF EXISTS public.push_job_batches;

ALTER TABLE public.push_jobs
    DROP COLUMN IF EXISTS batch_id;

ALTER TABLE public.push_jobs
    ADD COLUMN llm_meta_json jsonb;
