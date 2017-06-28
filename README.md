# TeXUtilServer

How to start the TeXUtilServer application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/tex-util-server-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

How to build the docker image
---

1. Run `mvn package`
1. Run `cp target/tex-util-server*.jar target/tex-util-server.jar`
1. Run `docker build -t huygensing/tex-util-server `.``
