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


--  GRANT ALL PRIVILEGES ON DATABASE temperature_db TO temperature_collector;
--BOOTSTRAP_SERVERS=127.0.0.1:9094;SPRING_DATASOURCE_PASSWORD=sK2rPjqfufz30jT;SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/;SPRING_DATASOURCE_USERNAME=temperature_collector;TEMPERATURE_TOPIC_NAME=temperatures
sK2rPjqfufz30jT
temperature_collector

--postgres=# GRANT pg_read_all_data TO temperature_collector;
--postgres=# GRANT pg_write_all_data TO temperature_collector;
