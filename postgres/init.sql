

CREATE TABLE event_logging(
	event_id serial PRIMARY KEY,
	event_data JSONB NOT NULL,
	additional_data JSONB NOT NULL,
	created_by VARCHAR(20) NOT NULL,
	created_on TIMESTAMP NOT NULL
);


GRANT SELECT, UPDATE, INSERT, DELETE ON event_logging TO postgres;