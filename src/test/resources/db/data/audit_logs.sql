INSERT INTO audit_logs (
    id,
    employee_id,
    created_at,
    created_by,
    updated_at,
    updated_by,
    last_action
)
VALUES (
    1,
    1,
    CURRENT_TIMESTAMP,
    'SYSTEM',
    NULL,
    NULL,
    'CREATE'
);