-- ��������� ������� `persons`

DROP TABLE IF EXISTS `persons`;
CREATE TABLE `persons` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '������������� ����������',
  `surname` varchar(60) NOT NULL COMMENT '�������',
  `name` varchar(60) NOT NULL COMMENT '���',
  `patronymic` varchar(60) NOT NULL COMMENT '��������',
  `dr` date NOT NULL COMMENT '���� ��������',
  `gender` varchar(1) NOT NULL COMMENT '���',
  `homePhone` varchar(18) DEFAULT NULL COMMENT '������� ��������',
  `workPhone` varchar(18) DEFAULT NULL COMMENT '������� �������',
  `mobilePhone` varchar(18) DEFAULT NULL COMMENT '������� ���������',
  `email` varchar(255) DEFAULT NULL COMMENT '����� ����������� �����',
  `semPol` tinyint(1) DEFAULT '0' NULL COMMENT '�������� ��������� 1- �����/0-������',
  `datPrin` date DEFAULT NULL COMMENT '���� �������� �� ������',
  `tabNo` varchar(10) DEFAULT NULL COMMENT '��������� �����',
  `dopsved` text COMMENT '�������������� ��������',
  `uvolen` tinyint(1) DEFAULT '0' NOT NULL COMMENT '������ - 1 / �������� - 0',
  `datUvol` date DEFAULT NULL COMMENT '���� ����������',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='������ ����������� ������' AUTO_INCREMENT=1;


INSERT INTO `persons` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `semPol`) VALUES ('1', '��������', '���������', '���������',   '1979-10-24', 'm','1');
INSERT INTO `persons` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `semPol`) VALUES ('2', '������',   '��������',  '�����������', '1966-09-24', 'm','1');
INSERT INTO `persons` (`id`, `surname`, `name`, `patronymic`, `dr`, `gender`, `semPol`) VALUES ('3', '��������', '���������', '�����������', '1972-02-23', 'm','1');


ALTER TABLE persons ADD `uvolen` tinyint(1) DEFAULT '0' NOT NULL COMMENT '������ - 1 / �������� - 0';
ALTER TABLE persons ADD `datUvol` date DEFAULT NULL COMMENT '���� ����������';