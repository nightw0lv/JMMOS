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
INSERT INTO `npcs` VALUES ('3', 'MONSTER', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('4', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('5', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('6', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('7', 'NPC', '1', '1', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('8', 'NPC', '1', '1', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('9', 'NPC', '1', '1', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('10', 'NPC', '1', '1', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('11', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('12', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('13', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('14', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('15', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('16', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('17', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('18', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('19', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('20', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('21', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('22', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('23', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('24', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('25', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('26', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('27', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('28', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('29', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('30', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('31', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('32', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('33', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('34', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('35', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('36', 'NPC', '1', '0', '100', '0', '0', '0', '0');
INSERT INTO `npcs` VALUES ('37', 'NPC', '1', '0', '100', '0', '0', '0', '0');
