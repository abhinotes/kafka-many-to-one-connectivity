bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic $1 --property print.key=true --property key.separator='|'