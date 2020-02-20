# kafka-training
kafka basic concepts with docker infra script

These samples need kafka and zookeeper running in localhost. We can use `docker-compose.yml` script avaiable in the repo the setup this infra (Docker should be installesd first).

## Infra setup
clone repo and perform below steps to setup infra.
 1.Execute `docker-compose up` from terminal. This should get the kafka and zookeeper up and running in your local.
 2.You can `modify docker-compose.yml` in case if you want to scale instances.

After above step, you should be able to execute the samples. 
 
## Additional Info
below are some of the comments for managing the kafka topics and instances.
Start zookeeper in local: bin/zookeeper-server-start.sh config/zookeeper.properties
Start kafka in local: bin/kafka-server-start.sh config/server.properties
Create topic: bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
List topics: bin/kafka-topics.sh --list --bootstrap-server localhost:9092
Alter topics: bin/kafka-topics.sh --zookeeper localhost:2181/chroot --alter --topic my_topic_name --partitions 3

More info on:https://kafka.apache.org/quickstart
