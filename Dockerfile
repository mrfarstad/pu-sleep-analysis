
FROM odinuge/maven-javafx:3-jdk-8
RUN mkdir /code
WORKDIR /code
COPY . /code/

RUN mvn -f tdt4140-gr1816/pom.xml install -DskipTests
RUN mvn -f tdt4140-gr1816/app.ui/pom.xml clean compile assembly:single
RUN mvn -f tdt4140-gr1816/app.api/pom.xml install -DskipTests
CMD mvn -f tdt4140-gr1816/app.api/pom.xml jetty:run

