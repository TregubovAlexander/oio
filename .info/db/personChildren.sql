-- ��������� ������� `personChildren`

DROP TABLE IF EXISTS `personChildren`;
CREATE TABLE IF NOT EXISTS `personChildren` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '������������� ������',
  `personId` int(11) NOT NULL COMMENT '������������� ����������',
  `childrenId` int(11) NOT NULL COMMENT '������������� �������',
  PRIMARY KEY (`id`),
  KEY `personId` (`personId`),
  KEY `childrenId` (`childrenId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='���� ����������� ����������' AUTO_INCREMENT=1 ;


INSERT INTO `personChildren` (`id`, `personId`, `childrenId`) VALUES
(1, 1, 1),
(2, 1, 2);
