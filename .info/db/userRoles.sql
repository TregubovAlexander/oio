-- Структура таблицы `userRoles`

DROP TABLE IF EXISTS `userRoles`;
CREATE TABLE `userRoles` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор записи',
  `userId` int(11) NOT NULL  COMMENT 'Идентификатор пользователя',
  `role` varchar(45) NOT NULL COMMENT 'Роль',
  PRIMARY KEY (`id`),
  UNIQUE KEY `usernameRole` (`role`,`userId`),
  KEY fk_username_idx (`userId`),
  CONSTRAINT fk_username FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Список пользователей' AUTO_INCREMENT=1;


INSERT INTO `userRoles` (`userId`, `role`) VALUES ('1', 'ROLE_USER');
INSERT INTO `userRoles` (`userId`, `role`) VALUES ('1', 'ROLE_ADMIN');

INSERT INTO `userRoles` (`userId`, `role`) VALUES ('2', 'ROLE_USER');
INSERT INTO `userRoles` (`userId`, `role`) VALUES ('2', 'ROLE_ADMIN');
INSERT INTO `userRoles` (`userId`, `role`) VALUES ('2', 'ROLE_DEVELOPER');

INSERT INTO `userRoles` (`userId`, `role`) VALUES ('3', 'ROLE_USER');

INSERT INTO `userRoles` (`userId`, `role`) VALUES ('4', 'ROLE_USER');
