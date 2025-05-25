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

<br>

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


<br>


## 3. deploy flink application with data platform server (deploy mode !)
1. `in resource file change kafka URL` and build `jar`
```
# /app/src/main/resources/.env
KAFKA_URL=kafka:9092
```

2. build container with [data platform server](https://github.com/BOKJUNSOO/goows-data-process-server)

<br>

## 4. now goows used topic (sample)

**직접 발행시 `줄바꿈` 자체가 메세지로 발행될 위험이 있습니다.**\
(줄바꿈 없이 사용 권장)
```json
{
  "memberId": 1,
  "keyword": "피자",
  "newsList": [
    {
      "title": "업비트, 윤남노 셰프와 함께 ‘업비트 <b>피자</b>’ 공개",
      "description": "가상자산 거래소 업비트를 운영하는 두나무가 윤남노 셰프(디핀)와 함께 ‘업비트 <b>피자</b>’를 선보인다고 19일 밝혔다. 업비트와 윤남노 셰프의 유쾌한 콜라보로 탄생한 ‘업비트 <b>피자</b>’는 비트코인을 연상시키는 국내산…",
      "pubDate": "Mon, 19 May 2025 08:16:00 +0900"
    },
    {
      "title": "업비트, 윤남노 셰프와 '업비트 <b>피자</b>' 만든다",
      "description": "국내 가상자산 거래소 업비트를 운영하는 두나무가 윤남노 셰프(디핀)와 함께 '업비트 <b>피자</b>'를 선보인다고 19일 밝혔다. 업비트 <b>피자</b>는 비트코인을 연상시키는 국내산 비트 100%를 토핑으로 사용했다. 비트코인 <b>피자</b>데이는…",
      "pubDate": "Mon, 19 May 2025 15:05:00 +0900"
    },
    {
      "title": "비트코인 <b>피자</b>데이…업비트·빗썸·코인원 이벤트(종합)",
      "description": "가상자산 거래소들이 19일 이른바 '비트코인 <b>피자</b>데이'를 앞두고 <b>피자</b> 업체들과 이벤트를 선보였다. 비트코인 <b>피자</b>데이는 미국 프로그래머가 2010년 5월 22일 <b>피자</b> 두 판을 1만 비트코인으로 결제한…",
      "pubDate": "Mon, 19 May 2025 13:10:00 +0900"
    },
    {
      "title": "도미노<b>피자</b>, 서울대어린이병원에 1억 원 기부",
      "description": "<b>피자</b>가 희망 나눔 캠페인을 통해 모인 적립금과 임직원들의 기부로 조성된 1억원을 서울대어린이병원에 전달했다. 이번 기부금은 통합케어센터 꿈틀꽃씨를 이용하는 환아들을 위해 사용될 예정이다.",
      "pubDate": "Mon, 19 May 2025 14:07:00 +0900"
    },
    {
      "title": "\"선착순 20만 명 <b>피자</b> 쏜다\"…빗썸, 도미노<b>피자</b>와 '<b>피자</b>데이' 이벤트 개시",
      "description": "가상자산 거래소 빗썸이 도미노<b>피자</b>와 선착순 20만 명을 대상으로 '더 강력해진 2025 비트코인 <b>피자</b>데이' 이벤트를 진행한다고 19일 밝혔다.",
      "pubDate": "Mon, 19 May 2025 17:22:00 +0900"
    }
  ]
}
```
