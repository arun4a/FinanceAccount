-- =====================================================================
-- Finance Account - schema
-- Target database: MySQL 8.x
-- =====================================================================
CREATE DATABASE IF NOT EXISTS sample CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE sample;

-- ---------------------------------------------------------------------
-- Application users
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
    `user_id`            INT AUTO_INCREMENT PRIMARY KEY,
    `user_name`          VARCHAR(100) NOT NULL,
    `user_phone_number`  VARCHAR(20),
    `password`           VARCHAR(255) NOT NULL,
    `user_address`       VARCHAR(500),
    `user_father_name`   VARCHAR(150),
    `sur_name`           VARCHAR(150),
    `first_name`         VARCHAR(150),
    UNIQUE KEY `uk_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `login_details` (
    `login_id`   INT AUTO_INCREMENT PRIMARY KEY,
    `user_id`    INT NOT NULL,
    `login_date` DATE,
    `login_time` TIME,
    `login_ip`   VARCHAR(64),
    `browser`    VARCHAR(200),
    KEY `idx_login_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Bond customers
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `customer` (
    `customer_id`          INT AUTO_INCREMENT PRIMARY KEY,
    `customer_name`        VARCHAR(150),
    `customer_fatherName`  VARCHAR(150),
    `cus_mobile_number`    VARCHAR(20),
    `cus_permanent_address` VARCHAR(500),
    `customer_occupation`  VARCHAR(100),
    `surety_name`          VARCHAR(150),
    `surety_address`       VARCHAR(500),
    `surety_mobile_number` VARCHAR(20),
    `surety_occupation`    VARCHAR(100),
    `surety_fname`         VARCHAR(150),
    `cheque`               VARCHAR(20),
    `passbook`             VARCHAR(20),
    `atm_submitted`        VARCHAR(20),
    `amount`               DECIMAL(15,2) DEFAULT 0,
    `date_of_issue`        DATE,
    `issued_by`            VARCHAR(50),
    `installments`         INT DEFAULT 0,
    `original_amount`      DECIMAL(15,2) DEFAULT 0,
    `account_number`       VARCHAR(50),
    `acountNo`             VARCHAR(50),
    `sur_rel`              VARCHAR(10),
    `cust_rel`             VARCHAR(10),
    `cust_second_mobno`    VARCHAR(20),
    `surety_second_mobno`  VARCHAR(20),
    `status`               VARCHAR(20) DEFAULT 'open',
    `closing_date`         DATE NULL,
    KEY `idx_customer_account` (`account_number`),
    KEY `idx_customer_status` (`status`),
    KEY `idx_customer_issue_date` (`date_of_issue`),
    KEY `idx_customer_closing_date` (`closing_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Bond installments / daily sheet entries
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `accounts` (
    `account_id`           INT AUTO_INCREMENT PRIMARY KEY,
    `deposit_amount`       DECIMAL(15,2) DEFAULT 0,
    `deposit_by_partner`   VARCHAR(150),
    `account_number`       VARCHAR(50),
    `customer_name`        VARCHAR(150),
    `installment`          INT,
    `deposit_name`         VARCHAR(150),
    `created_by`           VARCHAR(50),
    `date`                 DATE,
    `time`                 TIME,
    `customer_paid_amount` DECIMAL(15,2) DEFAULT 0,
    `amount`               DECIMAL(15,2) DEFAULT 0,
    `opening_balance`      DECIMAL(15,2) DEFAULT 0,
    `customer_id`          INT NULL,
    KEY `idx_accounts_account` (`account_number`),
    KEY `idx_accounts_date` (`date`),
    KEY `idx_accounts_customer` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Extra transactions
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `extra_transactions` (
    `transaction_id` INT AUTO_INCREMENT PRIMARY KEY,
    `amount`         DECIMAL(15,2) DEFAULT 0,
    `crt_date`       DATE,
    `crt_time`       TIME,
    `purpose`        VARCHAR(500),
    KEY `idx_extra_date` (`crt_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Deposits (In / Out)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `deposits` (
    `deposit_id`         INT AUTO_INCREMENT PRIMARY KEY,
    `deposit_name`       VARCHAR(150),
    `amount`             DECIMAL(15,2) DEFAULT 0,
    `deposit_account_no` VARCHAR(50),
    `created_date`       DATE,
    `time`               TIME,
    `mobile_number`      VARCHAR(20),
    `deposit_type`       VARCHAR(10),
    `interest`           DECIMAL(15,2) DEFAULT 0,
    `closing_date`       DATE NULL,
    `closing_time`       TIME NULL,
    KEY `idx_deposits_account` (`deposit_account_no`),
    KEY `idx_deposits_created` (`created_date`),
    KEY `idx_deposits_type` (`deposit_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Monthly interest payments for deposits
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `deposit_details` (
    `deposits_id`        INT AUTO_INCREMENT PRIMARY KEY,
    `crt_date`           DATE,
    `crt_time`           TIME,
    `deposit_account_no` VARCHAR(50),
    `amount`             DECIMAL(15,2) DEFAULT 0,
    `paid_amount`        DECIMAL(15,2) DEFAULT 0,
    `update_date`        DATE NULL,
    KEY `idx_deposit_details_account` (`deposit_account_no`),
    KEY `idx_deposit_details_crt_date` (`crt_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Monthly net amount snapshot (recalculated by scheduled job)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `net_amount_monthly` (
    `id`         INT AUTO_INCREMENT PRIMARY KEY,
    `date`       DATE,
    `net_amount` DECIMAL(15,2) DEFAULT 0,
    KEY `idx_net_amount_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
