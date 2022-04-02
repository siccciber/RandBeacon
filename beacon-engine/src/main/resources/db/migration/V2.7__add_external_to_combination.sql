ALTER TABLE vdf_combination
ADD COLUMN ext_src_id varchar(255) NOT NULL,
ADD COLUMN ext_status smallint(6)  NOT NULL,
ADD COLUMN ext_value varchar(255) NOT NULL;