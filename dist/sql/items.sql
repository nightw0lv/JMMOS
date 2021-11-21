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
INSERT INTO `items` VALUES ('5', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('7', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('10', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('11', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('12', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('13', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('14', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('15', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('17', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('18', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('19', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('20', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('21', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('22', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('23', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('24', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('25', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('27', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('32', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('35', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('36', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('37', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('38', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('39', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('40', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('41', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('42', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('43', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('44', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('45', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('47', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('49', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('50', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('52', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('53', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('54', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('55', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('56', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('57', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('60', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('61', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('62', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('63', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('64', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('65', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('66', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('67', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('68', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('69', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('70', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('71', 'HEAD', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('72', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('73', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('74', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('75', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('77', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('78', 'HANDS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('79', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('80', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('82', 'CHEST', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('84', 'LEGS', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('85', 'FEET', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('86', 'RIGHT_HAND', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `items` VALUES ('87', 'LEFT_HAND', 'EQUIP', '0', '0', '0', '0', '0', '0', '0', '0');