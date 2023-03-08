FROM openjdk:17-jdk-alpine

RUN mkdir -p /usr/share/otel/
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.23.0/opentelemetry-javaagent.jar /usr/share/otel/
RUN chmod u=x,g=x,o=x /usr/share/otel/opentelemetry-javaagent.jar

COPY target/spring-cloud-open-telemetry2-1.0.0-SNAPSHOT.jar spring-cloud-open-telemetry.jar

EXPOSE 8081

ENV OTEL_TRACES_EXPORTER=jaeger
ENV OTEL_EXPORTER_JAEGER_ENDPOINT=http://jaeger-service:14250
ENV OTEL_METRICS_EXPORTER=prometheus
ENV OTEL_EXPORTER_PROMETHEUS_PORT=9465

ENTRYPOINT ["java", "-javaagent:/usr/share/otel/opentelemetry-javaagent.jar","-jar","/spring-cloud-open-telemetry.jar"]