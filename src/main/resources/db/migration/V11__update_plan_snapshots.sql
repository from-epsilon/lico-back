ALTER TABLE public.plan_snapshots
    ADD COLUMN type varchar(32);

UPDATE public.plan_snapshots
SET type = 'CREATE'
WHERE type IS NULL;

ALTER TABLE public.plan_snapshots
    ALTER COLUMN type SET NOT NULL;

ALTER TABLE public.plan_snapshots
    ADD CONSTRAINT ck_plan_snapshots_type
        CHECK (type IN ('CREATE', 'UPDATE', 'STATUS_CHANGE'));

ALTER TABLE public.plans
    DROP CONSTRAINT IF EXISTS ck_plans_status;

ALTER TABLE public.plans
    ADD CONSTRAINT ck_plans_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'DELETED', 'DONE'));
