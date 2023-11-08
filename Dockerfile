FROM openjdk:8

WORKDIR /usrapp/bin

ENV PORT 6000

COPY SparkServer/target/classes /usrapp/bin/classes
COPY SparkServer/target/dependency /usrapp/bin/dependency

CMD ["java","-cp","./classes:./dependency/*","co.edu.escuelaing.SparkWebServer"]