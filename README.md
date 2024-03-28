## kontakt_io_task


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