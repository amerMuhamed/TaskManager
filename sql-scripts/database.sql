CREATE DATABASE IF NOT EXISTS `store_database` ;
USE `store_database`;

DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `members`;

CREATE TABLE `members` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login_id` varchar(50) NOT NULL,
  `pw` char(68) NOT NULL,
  `active` tinyint NOT NULL,
  `phone` varchar(15) NOT NULL UNIQUE,
  `email` varchar(30) NOT NULL UNIQUE,
  CONSTRAINT unique_login_id_pw UNIQUE (`login_id`, `pw`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Insert data into the members table
INSERT INTO `members` (login_id, pw, active, phone, email)
VALUES
('amer', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1, '01157789596', 'amermuhamed@gmail.com'),
('ali', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1, '01157789597', 'bmermuhamed@gmail.com'),
('omar', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1, '01157789598', 'cmermuhamed@gmail.com');

CREATE TABLE `tasks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description`  varchar(2000) DEFAULT NULL,
  `priority` varchar(50) DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `members` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `authorities5_idx_1` (`user_id`, `role`),
  CONSTRAINT `authorities5_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `members` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Insert data into the roles table
INSERT INTO `roles` (user_id, role)
VALUES
(2, 'ROLE_USER'),
(3, 'ROLE_USER'),
(1, 'ROLE_USER'),
(1, 'ROLE_ADMIN');

-- Reset AUTO_INCREMENT values if needed (optional)
ALTER TABLE `members` AUTO_INCREMENT = 1;
ALTER TABLE `tasks` AUTO_INCREMENT = 1;
ALTER TABLE `roles` AUTO_INCREMENT = 1;
