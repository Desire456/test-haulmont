CREATE TABLE IF NOT EXISTS doctors
(
    id                  BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name          VARCHAR(30),
    last_name           VARCHAR(30),
    patronymic          VARCHAR(30),
    specialization      VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS patients
(
    id                  BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name          VARCHAR(30),
    last_name           VARCHAR(30),
    patronymic          VARCHAR(30),
    telephone_number    VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS recipes
(
    id                  BIGINT IDENTITY NOT NULL PRIMARY KEY,
    description         LONGVARCHAR,
    patient_id          BIGINT NOT NULL,
    doctor_id           BIGINT NOT NULL,
    creation_date       TIMESTAMP,
    validity            INTEGER,
    priority            VARCHAR(6),

    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);