CREATE TABLE text_correction_tasks (
    id UUID PRIMARY KEY,
    original_text TEXT NOT NULL,
    language VARCHAR(2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    corrected_text TEXT,
    error_message TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);