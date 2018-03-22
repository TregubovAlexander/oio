-- Структура таблицы `users`

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор пользователя',
  `username` varchar(20) NOT NULL COMMENT 'Имя (псевдоним) пользователя',
  `password` varchar(60) NOT NULL COMMENT 'Пароль пользователя',
  `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT 'Разрешен / запрещен',
  `personId` int(11) COMMENT 'Идентификатор сотрудника (для связи)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Список пользователей' AUTO_INCREMENT=1;


INSERT INTO `users` (`id`, `username`, `password`, `enabled`, `personId`) VALUES ('1', 'admin', '$2a$04$Ig9jD2dw5T6UFOQEtAVCXeMnMO.rE0/96czEIfZVB0JNfrFzNh4vW', '1', '1');
INSERT INTO `users` (`id`, `username`, `password`, `enabled`, `personId`) VALUES ('2', 'alex',  '$2a$04$Umr9La2Xy13D0Ht2xRUnUuhIktI7nLao61efm1YtnZZq9u6GUYyUC', '1', '1');
INSERT INTO `users` (`id`, `username`, `password`, `enabled`, `personId`) VALUES ('3', 'Вася',  '$2a$04$Ig9jD2dw5T6UFOQEtAVCXeMnMO.rE0/96czEIfZVB0JNfrFzNh4vW', '1', '0');
INSERT INTO `users` (`id`, `username`, `password`, `enabled`, `personId`) VALUES ('4', 'Гена',  '$2a$04$Ig9jD2dw5T6UFOQEtAVCXeMnMO.rE0/96czEIfZVB0JNfrFzNh4vW', '1', '2');

