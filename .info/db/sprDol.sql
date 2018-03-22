-- ��������� ������� `spr_dol`

DROP TABLE IF EXISTS `sprDol`;
CREATE TABLE IF NOT EXISTS `sprDol` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '������������� ���������',
  `name` varchar(255) NOT NULL COMMENT '�������� ���������',
  `shortName` varchar(30) NOT NULL COMMENT '������� �������� ���������',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='���������� ����������' AUTO_INCREMENT=1 ;


INSERT INTO `sprDol` (`id`, `name`) VALUES
(1, '��������� ������ ��������������'),
(2, '������� �������-�����������'),
(3, '�������-�����������'),
(4, '�������-����������'),
(5, '������� �� ������ ����������'),
(6, '�������'),
(7, '�����������'),
(8, '��������� �������������'),
(9, '������-�����������'),
(10, '������'),
(11, '�������� ��������������� ������');

ALTER TABLE sprDol ADD `shortName` varchar(30) NOT NULL COMMENT '������� �������� ���������';