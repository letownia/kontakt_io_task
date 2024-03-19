CREATE TABLE room (
  id SERIAL PRIMARY KEY,
  room_name VARCHAR(64)
);


CREATE TABLE thermometer (
  id SERIAL PRIMARY KEY,
  thermometer_name VARCHAR(64) NOT NULL,
  room_id INTEGER REFERENCES room NOT NULL
);

CREATE TABLE temperature (
  id SERIAL PRIMARY KEY,
  thermometer_id INTEGER REFERENCES thermometer NOT NULL,
  timestamp_ms BIGINT NOT NULL,
  temperature FLOAT8 NOT NULL
);



CREATE USER temperature_collector WITH ENCRYPTED PASSWORD 'sK2rPjqfufz30jT';

GRANT all privileges on database temperature_db to temperature_collector ;
-- WARNING / TODO - next two lines should be changed. since it grants privileges on all databases
GRANT pg_read_all_data TO temperature_collector;
GRANT pg_write_all_data TO temperature_collector;


