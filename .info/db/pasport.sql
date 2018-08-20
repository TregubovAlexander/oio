-- Структура таблицы `pasport`

DROP TABLE IF EXISTS `pasport`;
CREATE TABLE IF NOT EXISTS `pasport` (
  `personId` int(11) NOT NULL COMMENT 'Идентификатор сотрудника',
  `ser` varchar(4) NOT NULL COMMENT 'Серия паспорта',
  `num` varchar(6) NOT NULL COMMENT 'Номер паспорта',
  `dat` date NOT NULL COMMENT 'Дата выдачи',
  `org` varchar(255) NOT NULL COMMENT 'Кем выдан',
  PRIMARY KEY (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Паспортные данные сотрудников';


INSERT INTO `pasport` (`personId`, `ser`, `num`, `dat`, `org`) VALUES
(1, '4202', '595095', '2002-09-12', 'ОВД г.Ельца Липецкой обл.');
