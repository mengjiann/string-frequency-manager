FROM openjdk:8-jre
VOLUME /var/www
EXPOSE 8080
RUN mkdir -p /var/www/string-frequency-manager
RUN mkdir -p /tmp
ENV TZ=Asia/Singapore
COPY ./target/string-frequency-manager-0.0.1-SNAPSHOT.jar  /var/www/string-frequency-manager/
ENTRYPOINT ["java","-jar","/var/www/string-frequency-manager/string-frequency-manager-0.0.1-SNAPSHOT.jar"]
