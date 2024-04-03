## kontakt_io_task

This project is an implementation of a recruitment task for kontakt io.

It is made of a couple of components  : 
- mock thermometers (see producer/ ) that send mock temperatures to a kafka stream
- kafka network
- postgres DB for storing temperatures and anomalies
- a temperature collector (see collector/) that collects temperatures, saves them to db, analyzes them for anomalies, and publishes info on anomalies

At the time of writing, it is still a work in progress.

# Zookeeper

Newer versions of kafka handle configuration internally and do not use zookeeper

# DB
In order to recreate DB - delete docker volume, run init_db.sql and sample_db_data.sql

i.e.
```

docker cp init_db.sql postgres_db:/tmp
docker cp sample_db_data.sql postgres_db:/tmp

docker exec postgres_db psql -U postgres -f /tmp/init_db.sql
docker exec postgres_db psql -U postgres -f /tmp/sample_db_data.sql


```


# Useful testing commands:

create network : `docker network create kafka_network_name --driver bridge`

```
I have no name!@02ce8cf6e963:/$ kafka-console-consumer.sh --topic temperatures --from-beginning --bootstrap-server kafka:9092
{"time":1.0,"temperature":21.0}
Processed a total of 1 messages
```