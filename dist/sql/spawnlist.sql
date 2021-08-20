CREATE TABLE IF NOT EXISTS `spawnlist` (
  `npc_id` int(4) DEFAULT '0',
  `x` double DEFAULT '0',
  `y` double DEFAULT '0',
  `z` double DEFAULT '0',
  `heading` double DEFAULT '0',
  `respawn_delay` int(4) DEFAULT '60'
) CHARSET=latin1 COLLATE=latin1_general_ci;

INSERT INTO `spawnlist` VALUES ('1', '4539.947', '68.558754', '3844.322', '246.68936', '60');
INSERT INTO `spawnlist` VALUES ('2', '4540.103', '68.70901', '3843.4163', '256.2401', '60');
