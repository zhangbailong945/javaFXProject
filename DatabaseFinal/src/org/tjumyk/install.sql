CREATE DATABASE IF NOT EXISTS `%DATABASE_NAME%`;

------

CREATE  TABLE IF NOT EXISTS `%DATABASE_NAME%`.`Class` (
  `classid` INT NOT NULL ,
  `classname` VARCHAR(45) NULL ,
  PRIMARY KEY (`classid`) ,
  UNIQUE INDEX `classname_UNIQUE` (`classname` ASC) );

------
  
CREATE  TABLE IF NOT EXISTS `%DATABASE_NAME%`.`Student` (
  `sid` BIGINT NOT NULL ,
  `sname` VARCHAR(45) NULL ,
  `gender` INT NULL ,
  `start_age` INT NULL ,
  `start_year` INT NULL ,
  `classid` INT NULL ,
  PRIMARY KEY (`sid`) ,
  UNIQUE INDEX `sid_UNIQUE` (`sid` ASC) ,
  INDEX `classid` (`classid` ASC) ,
  CONSTRAINT `classid`
    FOREIGN KEY (`classid` )
    REFERENCES `%DATABASE_NAME%`.`Class` (`classid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

------
	
CREATE  TABLE IF NOT EXISTS `%DATABASE_NAME%`.`Teacher` (
  `tid` INT NOT NULL ,
  `tname` VARCHAR(45) NULL ,
  PRIMARY KEY (`tid`) );

------  
  
CREATE  TABLE IF NOT EXISTS `%DATABASE_NAME%`.`Course` (
  `cid` INT NOT NULL ,
  `cname` VARCHAR(45) NULL ,
  `tid` INT NULL ,
  `point` INT NULL ,
  `min_grade` INT NULL ,
  `cancel_year` INT NULL ,
  PRIMARY KEY (`cid`) ,
  INDEX `tid` (`tid` ASC) ,
  CONSTRAINT `tid`
    FOREIGN KEY (`tid` )
    REFERENCES `%DATABASE_NAME%`.`Teacher` (`tid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
	
------	

CREATE  TABLE IF NOT EXISTS `%DATABASE_NAME%`.`TakeCourse` (
  `sid` BIGINT NOT NULL ,
  `cid` INT NOT NULL ,
  `year` INT NULL ,
  `score` INT NULL ,
  PRIMARY KEY (`sid`, `cid`) ,
  INDEX `sid` (`sid` ASC) ,
  INDEX `cid` (`cid` ASC) ,
  CONSTRAINT `sid`
    FOREIGN KEY (`sid` )
    REFERENCES `%DATABASE_NAME%`.`Student` (`sid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `cid`
    FOREIGN KEY (`cid` )
    REFERENCES `%DATABASE_NAME%`.`Course` (`cid` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

