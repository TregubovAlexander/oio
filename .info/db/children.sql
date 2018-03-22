-- ��������� ������� `children`

DROP TABLE IF EXISTS `children`;
CREATE TABLE IF NOT EXISTS `children` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '������������� ������',
  `surname` varchar(30) NOT NULL COMMENT '�������',
  `name` varchar(30) NOT NULL COMMENT '���',
  `patronymic` varchar(30) NOT NULL COMMENT '��������',
  `dr` date NOT NULL COMMENT '���� ��������',
  `gender` varchar(1) NOT NULL COMMENT '���',
  `birthSertificate` varchar(12) COMMENT '����� � ����� ������������� � ��������',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='���� ���� �����������' AUTO_INCREMENT=1 ;


INSERT INTO `children` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `birthSertificate`) VALUES
(1, '��������',  '����', '�������������', '2009-03-21', 'm', 'I��671591'),
(2, '���������', '����', '�������������', '2014-04-29', 'f', 'I��794038');
