INSERT INTO department (name)
VALUES
    ('IT'),
    ('Marketing'),
    ('HR');

INSERT INTO employees (name, age, department_id)
VALUES
    ('Anton', 22, 1),
    ('Kristina', 25, 2),
    ('Charlie', 35, 1),
    ('Diana', 28, 3),
    ('Sergey', 40, 2);

INSERT INTO employee_profiles (phone, emergency_contact, employee_id)
VALUES
    ('+7 912 345-67-89', '+7 999 888-77-66', 1),
    ('+7 922 333-44-55', '+7 911 222-33-44', 2),
    ('+7 900 123-45-67', '+7 905 678-90-12', 3),
    ('+7 987 654-32-10', '+7 999 123-45-67', 4),
    ('+7 909 876-54-32', '+7 910 111-22-33', 5);