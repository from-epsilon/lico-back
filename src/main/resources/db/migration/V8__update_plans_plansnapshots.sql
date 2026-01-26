DROP TABLE IF EXISTS public.chat_messages CASCADE;
DROP TABLE IF EXISTS public.plan_snapshots CASCADE;
DROP TABLE IF EXISTS public.plans CASCADE;

DROP SEQUENCE IF EXISTS public.chat_message_seq;
DROP SEQUENCE IF EXISTS public.plan_seq;
DROP SEQUENCE IF EXISTS public.plan_snapshot_seq;

CREATE TABLE public.plans (
  id uuid NOT NULL,
  user_id uuid NOT NULL,

  action text NOT NULL,
  purpose text,
  motive text,
  memo text,

  dtstart timestamptz NOT NULL,
  rrule text NOT NULL,

  remind boolean NOT NULL DEFAULT false,
  lead_time integer,
  current_version integer NOT NULL DEFAULT 0,
  current_snapshot_at  NOT NULL DEFAULT now(),

  status text NOT NULL DEFAULT 'ACTIVE',

  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  deleted_at timestamptz,

  CONSTRAINT plans_pkey PRIMARY KEY (id),

  CONSTRAINT fk_plans_user_id
    FOREIGN KEY (user_id) REFERENCES public.users(id),

  CONSTRAINT ck_plans_lead_time_nonneg
    CHECK (lead_time IS NULL OR lead_time >= 0),

  CONSTRAINT ck_plans_status
    CHECK (status IN ('ACTIVE', 'DELETED', 'DONE'))
);

CREATE INDEX idx_plans_user_id ON public.plans(user_id);
CREATE INDEX idx_plans_status ON public.plans(status);
CREATE INDEX idx_plans_dtstart ON public.plans(dtstart);

CREATE TABLE public.plan_snapshots (
  id uuid NOT NULL,
  plan_id uuid NOT NULL,

  version integer NOT NULL,
  snapshot_at timestamptz NOT NULL DEFAULT now(),
  data_json jsonb NOT NULL,

  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  deleted_at timestamptz,

  CONSTRAINT plan_snapshots_pkey PRIMARY KEY (id),

  CONSTRAINT fk_plan_snapshots_plan_id
    FOREIGN KEY (plan_id) REFERENCES public.plans(id) ON DELETE CASCADE,

  CONSTRAINT uk_plan_snapshots_plan_id_version
    UNIQUE (plan_id, version),

  CONSTRAINT ck_plan_snapshots_version_nonneg
    CHECK (version >= 0)
);

CREATE INDEX idx_plan_snapshots_plan_id_version ON public.plan_snapshots(plan_id, version);
CREATE INDEX idx_plan_snapshots_snapshot_at ON public.plan_snapshots(snapshot_at);

CREATE TABLE public.chat_messages (
  id bigint NOT NULL,

  plan_id uuid NOT NULL,
  snapshot_id uuid NOT NULL,
  snapshot_version integer NOT NULL,

  created_at timestamptz NOT NULL,
  updated_at timestamptz NOT NULL,
  deleted_at timestamptz,

  content text NOT NULL,
  type character varying(255) NOT NULL,

  CONSTRAINT chat_messages_pkey PRIMARY KEY (id),

  CONSTRAINT fk_chat_messages_plan_id
    FOREIGN KEY (plan_id) REFERENCES public.plans(id),

  CONSTRAINT fk_chat_messages_snapshot_id
    FOREIGN KEY (snapshot_id) REFERENCES public.plan_snapshots(id) ON DELETE CASCADE,

  CONSTRAINT chat_messages_type_check CHECK (
    (type)::text = ANY (
      ARRAY[
        ('NAGGING'::character varying)::text,
        ('PLAN_HISTORY'::character varying)::text,
        ('USER_REPLY'::character varying)::text
      ]
    )
  )
);

CREATE INDEX idx_chat_messages_plan_id_created_at
  ON public.chat_messages(plan_id, created_at);

CREATE SEQUENCE public.chat_message_seq
  START WITH 1
  INCREMENT BY 50
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

ALTER TABLE public.push_jobs
  DROP COLUMN IF EXISTS plan_id;

ALTER TABLE public.push_jobs
  ADD COLUMN plan_id uuid;

ALTER TABLE public.push_jobs
  ADD CONSTRAINT fk_push_jobs_plan_id
  FOREIGN KEY (plan_id) REFERENCES public.plans(id) ON DELETE SET NULL;
