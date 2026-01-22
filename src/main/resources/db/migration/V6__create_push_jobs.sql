CREATE TABLE public.push_job_batches (
    user_id uuid NOT NULL,
    batch_id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL DEFAULT now(),

    CONSTRAINT pk_push_job_batches PRIMARY KEY (user_id, batch_id),

    CONSTRAINT fk_push_job_batches_user_id
        FOREIGN KEY (user_id) REFERENCES public.users(id)
        ON DELETE CASCADE
);

CREATE TABLE public.push_jobs (
    id BIGSERIAL NOT NULL,
    batch_id uuid NOT NULL,
    user_id uuid NOT NULL,
    plan_id bigint NULL,
    kind varchar(20) NOT NULL,
    title text,
    body text,
    data_json jsonb,
    status varchar(20) NOT NULL DEFAULT 'READY',
    scheduled_at timestamp(6) with time zone NOT NULL,

    CONSTRAINT pk_push_jobs PRIMARY KEY (id),

    CONSTRAINT fk_push_jobs_batch
        FOREIGN KEY (user_id, batch_id)
        REFERENCES public.push_job_batches(user_id, batch_id)
        ON DELETE CASCADE,

    CONSTRAINT ck_push_jobs_kind
        CHECK (kind IN ('REMIND', 'GENERAL')),

    CONSTRAINT ck_push_jobs_status
        CHECK (status IN ('READY', 'RUNNING', 'DONE', 'FAILED'))
);

CREATE INDEX idx_push_jobs_ready_schedule
    ON public.push_jobs (scheduled_at ASC, id ASC)
    WHERE status = 'READY';

CREATE INDEX idx_push_jobs_user_ready_range
    ON public.push_jobs (user_id, scheduled_at)
    WHERE status = 'READY';

CREATE INDEX idx_push_jobs_batch
    ON public.push_jobs (user_id, batch_id);

CREATE INDEX idx_push_jobs_user
    ON public.push_jobs (user_id);
