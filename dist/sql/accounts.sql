CREATE TABLE IF NOT EXISTS `accounts` (
  `account` varchar(20) NOT NULL DEFAULT '',
  `password` varchar(64) NOT NULL, # MD5 hash
  `email` varchar(100) NOT NULL DEFAULT '',
  `first_name` varchar(45) NOT NULL DEFAULT '',
  `last_name` varchar(45) NOT NULL DEFAULT '',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_active` bigint(13) unsigned NOT NULL DEFAULT '0',
  `status` int(2) NOT NULL, # 1 banned, 2 requires activation, 3 active
  `membership` int(2) NOT NULL DEFAULT '0', # special status
  `last_ip` varchar(15) NULL DEFAULT NULL,
  `active_code` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`account`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
