-- Структура таблицы `spr_dol`

DROP TABLE IF EXISTS `sprDol`;
CREATE TABLE IF NOT EXISTS `sprDol` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор должности',
  `name` varchar(255) NOT NULL COMMENT 'Название должности',
  `shortName` varchar(30) NOT NULL COMMENT 'Краткое название должности',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Справочник должностей' AUTO_INCREMENT=1 ;


INSERT INTO `sprDol` (`id`, `name`) VALUES
(1, 'Начальник отдела информатизации'),
(2, 'Ведущий инженер-программист'),
(3, 'Инженер-программист'),
(4, 'Инженер-электроник'),
(5, 'Инженер по защите информации'),
(6, 'Инженер'),
(7, 'Программист'),
(8, 'Системный администратор'),
(9, 'Техник-программист'),
(10, 'Техник'),
(11, 'Директор вычислительного центра');

ALTER TABLE sprDol ADD `shortName` varchar(30) NOT NULL COMMENT 'Краткое название должности';