-- Pre-populate the tb_profile table with default profiles
-- Note: The table name is tb_profile and the sequence is primary_sequence as defined in the TbProfile entity.

INSERT INTO tb_profile (id, type, date_created, last_updated) 
VALUES (nextval('primary_sequence'), 'CLIENT', NOW(), NOW()) 
ON CONFLICT (type) DO NOTHING;

INSERT INTO tb_profile (id, type, date_created, last_updated) 
VALUES (nextval('primary_sequence'), 'OWNER', NOW(), NOW()) 
ON CONFLICT (type) DO NOTHING;