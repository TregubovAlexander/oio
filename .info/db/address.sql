-- ��������� ������� `adres`

DROP TABLE IF EXISTS `address`;
CREATE TABLE IF NOT EXISTS `address` (
  `personId` int(11) NOT NULL COMMENT '������������� ����������',
  `streetId` varchar(30) NOT NULL COMMENT '��� �� ����� (�� �����)',
  `zip` varchar(6) DEFAULT NULL COMMENT '������',
  `region` varchar(100) DEFAULT NULL COMMENT '������',
  `district` varchar(100) DEFAULT NULL COMMENT '�����',
  `city` varchar(50) NOT NULL COMMENT '�����',
  `street` varchar(100) DEFAULT NULL COMMENT '�����',
  `building` varchar(10) NOT NULL COMMENT '��������',
  `kvartira` int(3) DEFAULT NULL COMMENT '��������',
  `text` text NOT NULL COMMENT '������ �����',
  PRIMARY KEY (`personId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


INSERT INTO `address` (`personId`, `streetId`, `zip`, `region`, `district`, `city`, `street`, `building`, `kvartira`, `text`) VALUES
  (1, '48000002000015000', '399783', '��������', NULL, '����', '����������', '127�', 27, '399783, ���. ��������, �. ����, ��. ����������, �. 127�, ��. 27'),
  (2, '48000002000034900', '399782', '��������', NULL, '����', '���������', '3', 7, '399782, ���. ��������, �. ����, ��. ���������, �. 3, ��. 7'),
  (3, '48000002000033500', '399778', '��������', NULL, '����', '����������', '38', NULL, '399778, ���. ��������, �. ����, ��. ����������, �. 38'),
  (4, '23010000002001600', '353207', '�������������', '�������', '�������', '������', '11�', 4, '353207, ����. �������������, �-�. �������, �. �������, ��. ������, 11�, ��. 4')  ;

