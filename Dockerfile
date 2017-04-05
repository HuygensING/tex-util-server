FROM java:8
RUN apt-get update -q && apt-get install -qy \
    texlive-full
ENV wd=/home/texutil
RUN useradd -m texutil
USER texutil
RUN mkdir -p /home/texutil/scripts
WORKDIR ${wd}
ADD target/tex-util-server.jar ${wd}/
ADD config.yml ${wd}/
ADD scripts/tex2svg.sh ${wd}/scripts/
RUN chmod a+x scripts/tex2svg.sh
EXPOSE 8080 8081
ENTRYPOINT java -jar tex-util-server.jar server config.yml
