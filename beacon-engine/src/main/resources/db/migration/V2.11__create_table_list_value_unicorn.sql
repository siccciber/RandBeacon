CREATE TABLE list_value_unicorn (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  type varchar(255) DEFAULT NULL,
  uri varchar(255) DEFAULT NULL,
  value varchar(255) DEFAULT NULL,
  pulse_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_list_value_unicorn (pulse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE list_value_unicorn
ADD CONSTRAINT fk_list_value_unicorn_1
  FOREIGN KEY (pulse_id)
  REFERENCES vdf_unicorn (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;