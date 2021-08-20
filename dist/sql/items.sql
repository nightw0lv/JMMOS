CREATE TABLE IF NOT EXISTS `items` (
  `item_id` int(4) DEFAULT '0',
  `slot` enum('NONE','HEAD','CHEST','LEGS','HANDS','FEET','LEFT_HAND','RIGHT_HAND') DEFAULT 'NONE' NOT NULL,
  `type` enum('JUNK','EQUIP','CONSUME') DEFAULT 'JUNK' NOT NULL,
  `stackable` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `tradable` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `stamina` int(4) DEFAULT '0',
  `strength` int(4) DEFAULT '0',
  `dexterity` int(4) DEFAULT '0',
  `intelect` int(4) DEFAULT '0',
  `skill_id` int(4) DEFAULT '0',
  `skill_level` int(4) DEFAULT '0'
) CHARSET=latin1 COLLATE=latin1_general_ci;

INSERT INTO `items` VALUES ('1', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('2', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('3', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('4', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('5', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('6', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('7', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('8', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('9', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('10', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('11', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('12', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('13', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('14', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('15', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('16', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('17', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('18', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('19', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('20', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('21', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('22', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('23', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('24', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('25', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('26', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('27', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('28', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('29', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('30', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('31', 'RIGHT_HAND', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('32', 'LEFT_HAND', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');