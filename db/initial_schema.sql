CREATE DATABASE IF NOT EXISTS runner_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE runner_db;

--- users personal info and the application specific role
CREATE TABLE IF NOT EXISTS users
(
    --- Unique id of the user
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,

    ---  Role granted for the user with-in application context
    app_role VARCHAR(31) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    password_md5 VARCHAR(255) NOT NULL DEFAULT '',

    --- Meta info useful for audit and debugging
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id),
    UNIQUE KEY (email)
) ENGINE InnoDB;

--- Info on each run logged by users
CREATE TABLE IF NOT EXISTS user_runs
(
    run_id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,
    distance_in_km DECIMAL(7,3) NOT NULL DEFAULT 0,
    duration_in_hours DECIMAL(7,3) NOT NULL DEFAULT 0,
    lat_lng VARCHAR(50) NOT NULL,
    run_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    weather_conditions TEXT,

    --- Meta info useful for audit and debugging
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (run_id),
    INDEX user_idx (user_id, run_time)
) ENGINE InnoDB;

--- Reports generated for every week
CREATE TABLE IF NOT EXISTS weekly_run_reports
(
    report_id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,
    total_distance_in_km DECIMAL(10,3) NOT NULL DEFAULT 0,
    total_time_in_hours DECIMAL(10,3) NOT NULL DEFAULT 0,
    number_of_runs INT,

    avg_speed_kmph DECIMAL(7,3) NOT NULL DEFAULT 0,
    avg_distance_per_run_in_km DECIMAL(7,3) NOT NULL DEFAULT 0,
    avg_time_per_run_in_hour DECIMAL(7,3) NOT NULL DEFAULT 0,

    week_of_the_year TINYINT,
    run_year SMALLINT,
    week_first_day TIMESTAMP NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (report_id),
    INDEX user_idx (user_id, run_year, week_of_the_year)
) ENGINE InnoDB;

--- Reports generated for every month
CREATE TABLE IF NOT EXISTS monthly_run_reports
(
    report_id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,
    total_distance_in_km DECIMAL(10,3) NOT NULL DEFAULT 0,
    total_time_in_hours DECIMAL(10,3) NOT NULL DEFAULT 0,
    number_of_runs INT,

    avg_speed_kmph DECIMAL(7,3) NOT NULL DEFAULT 0,
    avg_distance_per_run_in_km DECIMAL(7,3) NOT NULL DEFAULT 0,
    avg_time_per_run_in_hour DECIMAL(7,3) NOT NULL DEFAULT 0,

    avg_distance_per_week_in_km DECIMAL(7,3) NOT NULL DEFAULT 0,
    avg_time_per_week_in_hour DECIMAL(7,3) NOT NULL DEFAULT 0,

    month_of_the_year TINYINT,
    run_year SMALLINT,
    month_first_day TIMESTAMP NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (report_id),
    INDEX user_idx (user_id, run_year, month_of_the_year)
) ENGINE InnoDB;