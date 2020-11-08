CREATE DATABASE IF NOT EXISTS `smbms`;
USE `smbms`;

CREATE TABLE IF NOT EXISTS `smbms_bill`(
	`id` INT(20) NOT NULL AUTO_INCREMENT,
    `billCode` VARCHAR(30) DEFAULT NULL,
    `productName` VARCHAR(30) DEFAULT NULL,
    `productDesc` VARCHAR(30) DEFAULT NULL,
    `productUnit` VARCHAR(30) DEFAULT NULL,
    `productCount` DECIMAL DEFAULT NULL,
    `totalPrice` DECIMAL DEFAULT NULL,
    `isPayment` INT(20) DEFAULT NULL,
    `createdBy` INT(20) DEFAULT NULL,
    `creationDate` DATETIME DEFAULT NULL,
    `modifyBy` INT(20) DEFAULT NULL,
    `modifyDate` DATETIME DEFAULT NULL,
    `providerId` INT(20) DEFAULT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `smbms_provider`(
	`id` INT(20) NOT NULL AUTO_INCREMENT,
    `proCode` VARCHAR(30) DEFAULT NULL,
    `proName` VARCHAR(30) DEFAULT NULL,
    `proDesc` VARCHAR(30) DEFAULT NULL,
    `proContact` VARCHAR(30) DEFAULT NULL,
    `proPhone` VARCHAR(30) DEFAULT NULL,
    `proAddress` VARCHAR(30) DEFAULT NULL,
    `createdBy` INT(20) DEFAULT NULL,
    `creationDate` DATETIME DEFAULT NULL,
    `modifyBy` INT(20) DEFAULT NULL,
    `modifyDate` DATETIME DEFAULT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `smbms_role`(
	`id` INT(20) NOT NULL AUTO_INCREMENT,
    `roleCode` VARCHAR(30) DEFAULT NULL,
    `roleName` VARCHAR(30) DEFAULT NULL,
    `createdBy` INT(20) DEFAULT NULL,
    `creationDate` DATETIME DEFAULT NULL,
    `modifyBy` INT(20) DEFAULT NULL,
    `modifyDate` DATETIME DEFAULT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `smbms_user`(
	`id` INT(20) NOT NULL AUTO_INCREMENT,
    `userCode` VARCHAR(30) DEFAULT NULL,
    `userName` VARCHAR(30) DEFAULT NULL,
    `userPassword` VARCHAR(30) DEFAULT NULL,
    `gender` INT(20) DEFAULT NULL,
    `birthday` DATETIME DEFAULT NULL,
    `phone` VARCHAR(30) DEFAULT NULL,
    `address` VARCHAR(30) DEFAULT NULL,
    `userRole` INT(20) DEFAULT NULL,
    `createdBy` INT(20) DEFAULT NULL,
    `creationDate` DATETIME DEFAULT NULL,
    `modifyBy` INT(20) DEFAULT NULL,
    `modifyDate` DATETIME DEFAULT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;