
--
-- Table structure for table `cab`
--

CREATE TABLE IF NOT EXISTS `cab` (
  `cabid` bigint NOT NULL AUTO_INCREMENT,
  `current_latitude` double DEFAULT NULL,
  `current_longitude` double DEFAULT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cabid`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

--
-- Dumping data for table `cab`
--


INSERT INTO `cab` VALUES (1,21.2095,72.781,'Hitesh'),(2,21.1997,72.7828,'Ravi'),(3,21.2078,72.792,'Kishan'),(4,21.221901,72.900063,'Shyam'),(5,21.1959,72.7933,'Vishal'),(6,21.1852,72.8095,'Mitesh'),(7,21.2021,72.8673,'Kalpesh'),(8,21.1068,72.7115,'Rahul');



--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

--
-- Dumping data for table `role`
--

INSERT INTO `role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES (1,_binary '',_binary '',_binary '',_binary '','{bcrypt}$2a$10$2xVH.y5VWHopTAUjGpKFAufEe.shHNcA9SwsAUKVMgqk/B5Ytr9Bi','admin'),(2,_binary '',_binary '',_binary '',_binary '',NULL,NULL),(3,_binary '',_binary '',_binary '',_binary '',NULL,NULL),(5,_binary '',_binary '',_binary '',_binary '','12345','jiggy71189'),(6,_binary '',_binary '',_binary '',_binary '','{bcrypt}$2a$10$JhiNK377Sata3j07oD9bV.2xFT5vS5pSTlje2IAz.kUxwyT6tI7O6','jiggy71189'),(7,_binary '',_binary '',_binary '',_binary '','{bcrypt}$2a$10$2LS823Mxp7nDcGqrm.H1z.tSpO00enydE3gjt0JqBvb5w9kyHA4EW','janki');

--
-- Table structure for table `user_roles`
--

CREATE TABLE IF NOT EXISTS `user_roles` (
  `users_id` bigint NOT NULL,
  `roles_id` bigint NOT NULL,
  PRIMARY KEY (`users_id`,`roles_id`),
  KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
  CONSTRAINT `FK7ecyobaa59vxkxckg6t355l86` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` VALUES (1,1),(5,1),(6,1),(7,2);

--
-- Table structure for table `usercabbooking`
--

CREATE TABLE IF NOT EXISTS `usercabbooking` (
  `bookingid` bigint NOT NULL AUTO_INCREMENT,
  `dest_latitude` double DEFAULT NULL,
  `dest_longitude` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `fare` double DEFAULT NULL,
  `source_latitude` double DEFAULT NULL,
  `source_longitude` double DEFAULT NULL,
  PRIMARY KEY (`bookingid`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

--
-- Dumping data for table `usercabbooking`
--

INSERT INTO `usercabbooking` VALUES (1,21.2111,72.863,5.282756889995153,NULL,21.2095,72.781);

