
SET DATABASE SQL SYNTAX PGS TRUE;
CREATE TYPE JSONB AS OTHER;
CREATE TABLE event_logging(
	event_id serial,
	event_data JSONB,
	additional_data JSONB,
	created_by VARCHAR(20),
	created_on TIMESTAMP,
	PRIMARY KEY (event_id)
);

GRANT SELECT, UPDATE, INSERT, DELETE ON event_logging TO sa;
