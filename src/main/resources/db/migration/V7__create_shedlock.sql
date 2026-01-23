CREATE TABLE IF NOT EXISTS shedlock (
  name        varchar(64)  NOT NULL PRIMARY KEY,
  lock_until  timestamp(6) with time zone  NOT NULL,
  locked_at   timestamp(6) with time zone  NOT NULL,
  locked_by   varchar(255) NOT NULL
);
