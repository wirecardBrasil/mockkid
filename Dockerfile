FROM openjdk:alpine

WORKDIR /root/
COPY target/mockkid.jar .
COPY ./src/main/resources/configuration /configs/

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "mockkid.jar", "--configuration.path=file:/configs/*.yaml" ]
