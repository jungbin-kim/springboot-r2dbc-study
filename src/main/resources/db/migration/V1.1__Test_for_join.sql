CREATE TABLE IF NOT EXISTS `tbl_team` (
  `team_id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`team_id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `tbl_member` (
  `member_id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NULL,
  `team_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`member_id`, `team_id`),
  INDEX `fk_tbl_member_tbl_team1_idx` (`team_id` ASC) ,
  CONSTRAINT `fk_tbl_member_tbl_team1`
    FOREIGN KEY (`team_id`)
    REFERENCES `tbl_team` (`team_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;