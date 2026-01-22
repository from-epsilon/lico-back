BEGIN;

ALTER TABLE public.user_details
    RENAME TO user_settings;

ALTER TABLE public.user_settings
    DROP COLUMN IF EXISTS core_value,
    DROP COLUMN IF EXISTS motive,
    DROP COLUMN IF EXISTS self_image;

ALTER TABLE public.user_settings
    ALTER COLUMN nickname SET NOT NULL,
    ALTER COLUMN verbosity_per_day SET NOT NULL,
    ALTER COLUMN sleep_time SET NOT NULL,
    ALTER COLUMN wake_time SET NOT NULL;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'pk_user_details') THEN
        ALTER TABLE public.user_settings
            RENAME CONSTRAINT pk_user_details TO pk_user_settings;
    END IF;

    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_user_details_user_id') THEN
        ALTER TABLE public.user_settings
            RENAME CONSTRAINT fk_user_details_user_id TO fk_user_settings_user_id;
    END IF;
END $$;

COMMIT;
