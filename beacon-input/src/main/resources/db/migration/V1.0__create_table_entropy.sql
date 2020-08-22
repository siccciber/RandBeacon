CREATE TABLE entropy (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  raw_data longtext NOT NULL,
  device_description varchar(50) NOT NULL,
  time_stamp datetime NOT NULL,
  period int NOT NULL,
  status_code int NOT NULL DEFAULT 0,
  sent BIT NOT NULL DEFAULT 0,
  noise_source TINYINT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;