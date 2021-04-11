-- For Testing Auto Generate Id
CREATE TABLE IF NOT EXISTS `tbl_person_2` (
  `person_id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`person_id`))
ENGINE = InnoDB;

-- MySQL 5.7 은 default uuid() 를 지원하지 않는다.
DELIMITER ;;
CREATE TRIGGER `tbl_person_2_before_insert`
BEFORE INSERT ON `tbl_person_2` FOR EACH ROW
BEGIN
  IF new.person_id IS NULL THEN
    SET new.person_id = uuid();
  END IF;
END;;
DELIMITER ;