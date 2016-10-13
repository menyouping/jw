use jay;
CREATE TABLE `dict_word` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT '0',
  `k` varchar(120) DEFAULT NULL,
  `v` varchar(510) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dict_word_k_idx` (`k`),
  KEY `dict_word_type_idx` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=36672 DEFAULT CHARSET=utf8