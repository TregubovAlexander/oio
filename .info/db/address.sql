-- Структура таблицы `adres`

DROP TABLE IF EXISTS `address`;
CREATE TABLE IF NOT EXISTS `address` (
  `personId` int(11) NOT NULL COMMENT 'Идентификатор сотрудника',
  `streetId` varchar(30) NOT NULL COMMENT 'Код по КЛАДР (до улицы)',
  `zip` varchar(6) DEFAULT NULL COMMENT 'Индекс',
  `region` varchar(100) DEFAULT NULL COMMENT 'Регион',
  `district` varchar(100) DEFAULT NULL COMMENT 'Район',
  `city` varchar(50) NOT NULL COMMENT 'Город',
  `street` varchar(100) DEFAULT NULL COMMENT 'Улица',
  `building` varchar(10) NOT NULL COMMENT 'Строение',
  `kvartira` int(3) DEFAULT NULL COMMENT 'Квартира',
  `text` text NOT NULL COMMENT 'Полный адрес',
  PRIMARY KEY (`personId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


INSERT INTO `address` (`personId`, `streetId`, `zip`, `region`, `district`, `city`, `street`, `building`, `kvartira`, `text`) VALUES
  (1, '48000002000015000', '399783', 'Липецкая', NULL, 'Елец', 'Коммунаров', '127А', 27, '399783, обл. Липецкая, г. Елец, ул. Коммунаров, д. 127А, кв. 27'),
  (2, '48000002000034900', '399782', 'Липецкая', NULL, 'Елец', 'Юбилейная', '3', 7, '399782, обл. Липецкая, г. Елец, ул. Юбилейная, д. 3, кв. 7'),
  (3, '48000002000033500', '399778', 'Липецкая', NULL, 'Елец', 'Чернавская', '38', NULL, '399778, обл. Липецкая, г. Елец, ул. Чернавская, д. 38'),
  (4, '23010000002001600', '353207', 'Краснодарский', 'Динской', 'Агроном', 'Ленина', '11А', 4, '353207, край. Краснодарский, р-н. Динской, п. Агроном, ул. Ленина, 11А, кв. 4')  ;

