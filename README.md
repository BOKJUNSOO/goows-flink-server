# goows flink server



## 1. localtest without flink container (dev mode - fastrun)
1. `in resource file change kafka URL` and build `jar`
```
# /app/src/main/resources/.env
KAFKA_URL=localhost:9092
```

2. build kafka container with [kafka server](https://github.com/BOKJUNSOO/goows-kafka-server)

3. submit job
```bash
./gradlew run
```

4. send message with kafka container
- check how to send message in kafka topic with README.md [kafkaserver](https://github.com/BOKJUNSOO/goows-kafka-server)


## 2. localtest with flink container (dev mode - check flink container and submit job)

1. `in resource file change kafka URL` and build `jar`
```
# /app/src/main/resources/.env
KAFKA_URL=kafka:9093
```
```bash
./gradlew clean build
```
2. make container network
```bash
docker network create goows
```
3. build kafka container with [kafka server](https://github.com/BOKJUNSOO/goows-kafka-server)

4. submit job
```bash
docker exec -it goows-flink-server-1 /bin/bash

# in container /opt/flink/job directory
$ chmod 777 ./flinksubmit.sh
$ ./flinksubmit.sh
```

5. send message with kafka container
- check how to send message in kafka topic with README.md [kafkaserver](https://github.com/BOKJUNSOO/goows-kafka-server)

## 3. deploy flink application with data platform server (deploy mode !)
1. `in resource file change kafka URL` and build `jar`
```
# /app/src/main/resources/.env
KAFKA_URL=kafka:9092
```

2. build container with [data platform server]([www.naver.com](https://github.com/BOKJUNSOO/goows-data-process-server))