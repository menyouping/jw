USE jay;
CREATE TABLE `dict_word` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT '0',
  `k` varchar(120) DEFAULT NULL,
  `v` varchar(2500) DEFAULT NULL,
  `agent` varchar(10) DEFAULT NULL,
  `version` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `dict_word_k_idx` (`k`),
  KEY `dict_word_type_idx` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=36697 DEFAULT CHARSET=utf8