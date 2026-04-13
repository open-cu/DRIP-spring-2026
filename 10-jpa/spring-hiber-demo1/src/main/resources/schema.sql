CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER NOT NULL
);

CREATE TABLE employee_profile (
    id BIGSERIAL PRIMARY KEY,
    phone TEXT NOT NULL,
    emergency_contact TEXT NOT NULL,
    employee_id BIGINT UNIQUE NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);