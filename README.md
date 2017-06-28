# TeXUtilServer

REST-based server dealing with LaTeX
Currently only compiles LaTeX code to SVG.


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


How to run the docker image
---
`docker run -d huygensing/tex-util-server -p {local_main_port}:8080 -p {local_admin_port}:8081 -e BASE_URI=http://{local_server_name}:{local_main_port}`


How to generate SVG from LaTeX
---
POST the LaTeX code as `Content-type: text/plain` to `http://{local_server_name}:{local_main_port}/2svg`
This call will, on success, return the URL to the generated svg in a `Location`: header.
The TeX code uploaded, the generated SVG and all the intermediary files will be deleted after 1 hour.
