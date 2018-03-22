-- ��������� ������� `tabelSpecialDays`

DROP TABLE IF EXISTS `tabelSpecialDays`;
CREATE TABLE `tabelSpecialDays` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '������������� ���������� ���',
  `personId` int(11) NOT NULL COMMENT '������������� ����������',
  `dateBegin` date NOT NULL COMMENT '���� ������ ��������� ����',
  `dateEnd` date NOT NULL COMMENT '���� ��������� ��������� ����',
  `kod` int(11) NOT NULL COMMENT '��� ���������� ��� - ID � ����������� SprTabelNotation',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='������ �������� �� ������� ���� � ������' AUTO_INCREMENT=1;


INSERT INTO `tabelSpecialDays` (`id`, `personId`, `dateBegin`, `dateEnd`, `kod`) VALUES 
('1', '8', '2015-11-23', '2018-09-11', 15),
('2', '18', '2018-01-23', '2019-01-20', 15),
('3', '16', '2018-02-06', '2018-02-07', 6),
('4', '16', '2018-02-12', '2018-02-26', 19);


