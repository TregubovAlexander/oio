-- ��������� ������� `tabelHolidays`

DROP TABLE IF EXISTS `tabelHolidays`;
CREATE TABLE `tabelHolidays` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '�������������',
  `year` INT(4) NOT NULL DEFAULT 0 COMMENT '��� ����������� ����',
  `month` TINYINT UNSIGNED NOT NULL COMMENT '����� ����������� ����',
  `day` TINYINT UNSIGNED NOT NULL COMMENT '���� ����������� ����',
  `isHoliday` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '��� ����������� ���� (1 - ��������, 0 - ������ � �������� ����',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='������ ����������� ���� ��� ������' AUTO_INCREMENT=1;


INSERT INTO `tabelHolidays` (`year`, `month`, `day`, `isHoliday`) VALUES 
(0,    1,  1,  1),
(0,    1,  2,  1),
(0,    1,  3,  1),
(0,    1,  6,  1),
(0,    2,  23, 1),
(0,    3,  8,  1),
(0,    5,  1,  1),
(0,    5,  2,  1),
(0,    5,  9,  1),
(0,    6,  12, 1),
(0,    11, 4,  1),

(2018, 1,  4,  1),
(2018, 1,  5,  1),
(2018, 1,  7,  1),
(2018, 1,  8,  1),

(2018, 3,  9,  1),

(2018, 4,  28, 0),
(2018, 4,  30, 1),

(2018, 6,  9,  0),
(2018, 6,  11, 1),
(2018, 11, 5,  1),
(2018, 12, 29, 0),
(2018, 12, 31, 1);
