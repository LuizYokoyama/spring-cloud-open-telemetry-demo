OpenTelemetry demo


Instruções:

nas pastas spring-cloud-open-telemetry1 e spring-cloud-open-telemetry2

execute:

mvn package -Dmaven.test.skip

depois nesta pasta execute:

docker-compose up --build

Acesse os endpoints:

http://localhost:8080/product/100001

http://localhost:8080/product/100002

...

http://localhost:8080/product/100004

e http://localhost:8080/product/10000{acima de 4 pra testar erro}

Visualize os traces em:

http://localhost:16686/

## Relevant Articles
- [OpenTelemetry Setup in Spring Boot Application](https://www.baeldung.com/spring-boot-opentelemetry-setup)
