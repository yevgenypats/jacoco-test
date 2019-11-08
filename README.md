```bash
docker run -v `pwd`:/app -it maven:3.6.2-jdk-11 /bin/bash
cd /app
mvn install
MAVEN_OPTS="-javaagent:/app/src/main/resources/jacocoagent.jar" mvn org.yevgeny:jacocobug:bug


```