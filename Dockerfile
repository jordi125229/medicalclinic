FROM amazoncorretto:21-alpine
LABEL maintener="Piotr"
COPY target/medicalclinic-0.0.1-SNAPSHOT.jar app/medical-clinic.jar
ENTRYPOINT ["java","-jar","app/medical-clinic.jar"]