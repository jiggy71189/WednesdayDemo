
CREATE TABLE  `cab` (
  `cabid` bigint NOT NULL AUTO_INCREMENT,
  `current_latitude` double DEFAULT NULL,
  `current_longitude` double DEFAULT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cabid`)
) ;



CREATE TABLE  `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;



INSERT INTO `role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');



CREATE TABLE  `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;


INSERT INTO `user` VALUES (1,1,1,1,1,'{bcrypt}$2a$10$2LS823Mxp7nDcGqrm.H1z.tSpO00enydE3gjt0JqBvb5w9kyHA4EW','admin');



CREATE TABLE `user_roles` (
  `users_id` bigint NOT NULL,
  `roles_id` bigint NOT NULL,
  PRIMARY KEY (`users_id`,`roles_id`),
  KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
  CONSTRAINT `FK7ecyobaa59vxkxckg6t355l86` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`)
); 



INSERT INTO `user_roles` VALUES (1,1);



CREATE TABLE `usercabbooking` (
  `bookingid` bigint NOT NULL AUTO_INCREMENT,
  `dest_latitude` double DEFAULT NULL,
  `dest_longitude` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `fare` double DEFAULT NULL,
  `source_latitude` double DEFAULT NULL,
  `source_longitude` double DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`bookingid`),
  KEY `cab_user_id` (`user_id`),
  CONSTRAINT `cab_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
); 




