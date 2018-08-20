-- Структура таблицы `posts`

DROP TABLE IF EXISTS `posts`;
CREATE TABLE IF NOT EXISTS `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор записи',
  `personId` int(11) NOT NULL COMMENT 'Идентификатор сотрудника ',
  `dolId` int(11) NOT NULL COMMENT 'Идентификатор должности из справочника должностей sprDol',
  `dateBegin` date NOT NULL COMMENT 'Дата назначения на должность',
  `dateEnd` date DEFAULT NULL COMMENT 'Дата назначения на должность',
  `stavka` float DEFAULT NULL COMMENT 'Число ставок',
  `active` TINYINT NOT NULL DEFAULT 1 COMMENT 'Активная / Неактивная (Действующая / Архивная)',
  PRIMARY KEY (`id`),
  KEY `personId` (`personId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Список должностей, занимаемых сотрудниками в организации' AUTO_INCREMENT=1 ;


INSERT INTO `posts` (`id`, `personId`,`dolId`,`dateBegin`,`dateEnd`,`stavka`, `active`) VALUES
(1, '1','1','2016-12-20',NULL,'1','1'),
(2, '1','11','2014-01-09','2016-12-19','1','0'),
(3, '1','7','2002-10-01','2014-01-08','1','0'),
(4, '3','2','2016-12-20',NULL,'1','1');