-- =====================================================================
-- Finance Account - seed data
-- Default login: admin / admin123
-- The password is stored using the legacy PBEWithMD5AndDES scheme so
-- existing installations keep working after the migration.
-- =====================================================================
USE sample;

INSERT INTO `user` (`user_name`, `user_phone_number`, `password`, `user_address`, `user_father_name`, `sur_name`, `first_name`)
SELECT 'admin', '0000000000', 'GhrmCpG5ZtPNHY7DgwofvQ==', 'Head Office', 'Administrator', 'Admin', 'System'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `user_name` = 'admin');
