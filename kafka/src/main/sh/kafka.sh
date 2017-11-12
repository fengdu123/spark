[list]
kafka-topics --list --zookeeper  localhost:2181
[create]
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic first
[product]
kafka-console-producer --broker-list localhost:9092 --topic first
[comsumer]
kafka-console-consumer --zookeeper localhost:2181 --from-beginning --topic first