-- Структура таблицы `persons`

DROP TABLE IF EXISTS `persons`;
CREATE TABLE `persons` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор сотрудника',
  `surname` varchar(60) NOT NULL COMMENT 'Фамилия',
  `name` varchar(60) NOT NULL COMMENT 'Имя',
  `patronymic` varchar(60) NOT NULL COMMENT 'Отчество',
  `dr` date NOT NULL COMMENT 'Дата рождения',
  `gender` varchar(1) NOT NULL COMMENT 'Пол',
  `homePhone` varchar(18) DEFAULT NULL COMMENT 'Телефон домашний',
  `workPhone` varchar(18) DEFAULT NULL COMMENT 'Телефон рабочий',
  `mobilePhone` varchar(18) DEFAULT NULL COMMENT 'телефон мобильный',
  `email` varchar(255) DEFAULT NULL COMMENT 'Адрес электронной почты',
  `semPol` tinyint(1) DEFAULT '0' NULL COMMENT 'Семейное положение 1- женат/0-холост',
  `datPrin` date DEFAULT NULL COMMENT 'Дата принятия на работу',
  `tabNo` varchar(10) DEFAULT NULL COMMENT 'Табельный номер',
  `dopsved` text COMMENT 'Дополнительные сведения',
  `uvolen` tinyint(1) DEFAULT '0' NOT NULL COMMENT 'Уволен - 1 / Работает - 0',
  `datUvol` date DEFAULT NULL COMMENT 'Дата увольнения',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Список сотрудников отдела' AUTO_INCREMENT=1;


INSERT INTO `persons` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `semPol`) VALUES ('1', 'Трегубов', 'Александр', 'Борисович',   '1979-10-24', 'm','1');
INSERT INTO `persons` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `semPol`) VALUES ('2', 'Числов',   'Геннадий',  'Анатольевич', '1966-09-24', 'm','1');
INSERT INTO `persons` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `semPol`) VALUES ('3', 'Самойлов', 'Александр', 'Анатольевич', '1972-02-23', 'm','1');


ALTER TABLE persons ADD `uvolen` tinyint(1) DEFAULT '0' NOT NULL COMMENT 'Уволен - 1 / Работает - 0';
ALTER TABLE persons ADD `datUvol` date DEFAULT NULL COMMENT 'Дата увольнения';