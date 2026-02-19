ALTER TABLE user_settings
    ADD COLUMN tier VARCHAR(32) NOT NULL DEFAULT 'BASIC';

ALTER TABLE user_settings
    ADD CONSTRAINT ck_user_settings_tier
        CHECK (tier IN ('BASIC', 'PREMIUM'));