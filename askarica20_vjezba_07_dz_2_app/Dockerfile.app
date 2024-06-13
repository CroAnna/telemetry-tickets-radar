FROM amd64/eclipse-temurin:21

RUN apt-get update && \
    apt-get install -y wget unzip

WORKDIR /app

COPY . /app

EXPOSE 8080

COPY ./pokreni.sh /pokreni.sh
RUN chmod -R 777 /pokreni.sh

ENTRYPOINT ["/pokreni.sh"]