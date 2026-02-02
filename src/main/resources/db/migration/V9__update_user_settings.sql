ALTER TABLE user_settings
    ADD COLUMN timezone VARCHAR(64);

UPDATE user_settings
SET timezone = 'Asia/Seoul'
WHERE timezone IS NULL;

ALTER TABLE user_settings
    ALTER COLUMN timezone SET NOT NULL;
