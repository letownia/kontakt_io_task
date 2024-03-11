## kontakt_io_task

# Zookeeper

Newer versions of kafka handle configuration internally and do not use zookeeper

# Useful testing commands:

```
I have no name!@02ce8cf6e963:/$ kafka-console-consumer.sh --topic temperatures --from-beginning --bootstrap-server kafka:9092
{"time":1.0,"temperature":21.0}
Processed a total of 1 messages
```