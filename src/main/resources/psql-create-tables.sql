CREATE TABLE scenario (
    id_scenario BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    scenario_name VARCHAR(50) NOT NULL,
    capacity BIGINT NOT NULL CHECK (capacity > 0),
    period TSRANGE NOT NULL
);

CREATE TABLE bussiness_hour(
    id_bussiness_hour BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    activated BOOLEAN NOT NULL DEFAULT TRUE,
    start_week_day SMALLINT NOT NULL CHECK (start_week_day > 0 AND start_week_day < 8),
    end_week_day SMALLINT NOT NULL CHECK (end_week_day > 0 AND end_week_day < 8),
    start_time TIME WITHOUT TIME ZONE NOT NULL,
    end_time TIME WITHOUT TIME ZONE NOT NULL,
    id_scenario BIGINT NOT NULL REFERENCES scenario (id_scenario)
);

CREATE TABLE price_model(
    id_price_model BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model_name VARCHAR(50) NOT NULL,
    activated BOOLEAN NOT NULL DEFAULT TRUE,
    price NUMERIC NOT NULL CHECK (price > 0),
    id_scenario BIGINT NOT NULL REFERENCES scenario (id_scenario)
);

CREATE TABLE operation(
    id_operation BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    entry TIMESTAMP NOT NULL,
    leave TIMESTAMP,
    plate VARCHAR(7) NOT NULL CHECK (plate ~ '[A-Z]{3}[0-9]{4}' OR plate ~ '[A-Z]{3}[0-9][A-Z][0-9]{2}'),
    total NUMERIC CHECK (total > -1),
    id_price_model INTEGER NOT NULL REFERENCES price_model (id_price_model)
);

CREATE TABLE estaciona_role(
    id_role INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE estaciona_user(
    id_user INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    password_hash TEXT NOT NULL,
    id_role INT NOT NULL REFERENCES estaciona_roles (id_role)
);

