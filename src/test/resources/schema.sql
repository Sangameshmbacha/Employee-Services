CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
 
CREATE TABLE IF NOT EXISTS designations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
 
CREATE TABLE IF NOT EXISTS skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
 
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id VARCHAR(50),
    project_name VARCHAR(255)
);
 
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE,
    nationality VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    country_code VARCHAR(10),
    phone_number BIGINT,
    gender VARCHAR(20),
    department_id BIGINT,
    designation_id BIGINT,
 
    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id) REFERENCES departments(id),
 
    CONSTRAINT fk_employee_designation
        FOREIGN KEY (designation_id) REFERENCES designations(id)
);
 
CREATE TABLE IF NOT EXISTS employment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT,
    date_of_joining DATE,
    employment_type VARCHAR(50),
    is_active BOOLEAN,
    manager_id BIGINT,
    mode VARCHAR(50),
    probation_period_months INT,
    status VARCHAR(50),
 
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);
 
CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT,
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    address_type VARCHAR(50),
 
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);
 
CREATE TABLE IF NOT EXISTS employee_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT,
    skill_id BIGINT,
    level VARCHAR(50),
    yoe INT,
 
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (skill_id) REFERENCES skill(id)
);
 
CREATE TABLE IF NOT EXISTS employee_projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT,
    project_id BIGINT,
    role VARCHAR(255),
    allocation_percentage INT,
 
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);
 
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT,
    created_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),
    last_action VARCHAR(50),
 
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);
 