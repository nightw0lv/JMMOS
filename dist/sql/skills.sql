CREATE TABLE IF NOT EXISTS `skills` (
  `skill_id` int(4) DEFAULT '0',
  `level` int(4) DEFAULT '1',
  `type` enum('DUMMY','DAMAGE','RESTORE_HP','RESTORE_MP') DEFAULT 'DUMMY' NOT NULL,
  `reuse` int(4) DEFAULT '0',
  `range` int(4) DEFAULT '0',
  `param_1` int(4) DEFAULT '0',
  `param_2` int(4) DEFAULT '0'
) CHARSET=latin1 COLLATE=latin1_general_ci;