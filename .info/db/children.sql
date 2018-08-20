-- Структура таблицы `children`

DROP TABLE IF EXISTS `children`;
CREATE TABLE IF NOT EXISTS `children` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор записи',
  `surname` varchar(30) NOT NULL COMMENT 'Фамилия',
  `name` varchar(30) NOT NULL COMMENT 'Имя',
  `patronymic` varchar(30) NOT NULL COMMENT 'Отчество',
  `dr` date NOT NULL COMMENT 'Дата рождения',
  `gender` varchar(1) NOT NULL COMMENT 'Пол',
  `birthSertificate` varchar(12) COMMENT 'Серия и номер свидетельства о рождении',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Дети всех сотрудников' AUTO_INCREMENT=1 ;


INSERT INTO `children` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `birthSertificate`) VALUES
(1, 'Трегубов',  'Иван', 'Александрович', '2009-03-21', 'm', 'IРД671591'),
(2, 'Трегубова', 'Анна', 'Александровна', '2014-04-29', 'f', 'IРД794038');

