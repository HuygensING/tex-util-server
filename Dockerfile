FROM java:8
RUN apt-get update -q && apt-get install -qy \
    texlive
ENV wd=/home/texutil
RUN useradd -m texutil
RUN mkdir -p /home/texutil/scripts
WORKDIR ${wd}
ADD target/tex-util-server.jar ${wd}/
ADD config.yml ${wd}/
ADD scripts/tex2svg.sh ${wd}/scripts/
RUN chmod a+x ${wd}/scripts/tex2svg.sh
RUN chown -R texutil .
EXPOSE 8080 8081
USER texutil
ENTRYPOINT java -jar tex-util-server.jar server config.yml
