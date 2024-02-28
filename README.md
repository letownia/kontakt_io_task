## kontakt_io_task

# Start zookeeper

To start zookeeper run ( https://kafka.apache.org/quickstart ):
```
borg@borgs-MacBook-Pro:~/Desktop/kafka_2.13-3.6.1$ ~/Desktop/kafka_2.13-3.6.1/bin/zookeeper-server-start.sh config/zookeeper.properties
```
Or using Docker : 
```
docker run --name some-zookeeper --restart always -d zookeeper:3.7.2
```
- we probably want zookeeper to be in 'standalone mode'  (no redundancy, and such)

# Start the Kafka broker service
$ bin/kafka-server-start.sh config/server.properties