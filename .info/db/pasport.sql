-- ��������� ������� `pasport`

DROP TABLE IF EXISTS `pasport`;
CREATE TABLE IF NOT EXISTS `pasport` (
  `personId` int(11) NOT NULL COMMENT '������������� ����������',
  `ser` varchar(4) NOT NULL COMMENT '����� ��������',
  `num` varchar(6) NOT NULL COMMENT '����� ��������',
  `dat` date NOT NULL COMMENT '���� ������',
  `org` varchar(255) NOT NULL COMMENT '��� �����',
  PRIMARY KEY (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='���������� ������ �����������';


INSERT INTO `pasport` (`personId`, `ser`, `num`, `dat`, `org`) VALUES
(1, '4202', '595095', '2002-09-12', '��� �.����� �������� ���.');
