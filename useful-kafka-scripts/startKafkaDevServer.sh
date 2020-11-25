nohup bin/zookeeper-server-start.sh config/zookeeper.properties &> logs/zookeeper.nohup.out &
nohup bin/kafka-server-start.sh config/server.properties &> logs/kafka-server.nohup.out &

