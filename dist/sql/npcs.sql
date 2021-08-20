CREATE TABLE IF NOT EXISTS `npcs` (
  `npc_id` int(4) DEFAULT '0',
  `type` enum('NPC','MONSTER') DEFAULT 'NPC' NOT NULL,
  `level` int(4) DEFAULT '1',
  `sex` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `hp` int(8) DEFAULT '100',
  `stamina` int(4) DEFAULT '0',
  `strength` int(4) DEFAULT '0',
  `dexterity` int(4) DEFAULT '0',
  `intelect` int(4) DEFAULT '0'
) CHARSET=latin1 COLLATE=latin1_general_ci;

INSERT INTO `npcs` VALUES ('1', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('2', 'NPC', '1', '1', '100', '0', '0', '0', '0');