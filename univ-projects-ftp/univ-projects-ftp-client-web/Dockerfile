FROM tomcat:9-jre8

LABEL maintainer=Matschieu

WORKDIR ${CATALINA_HOME}/webapps

COPY target/univ-projects-ftp-client-web-1.0.0-SNAPSHOT.war ROOT.war

EXPOSE 8080

HEALTHCHECK CMD curl http://localhost/index.html --fail > /dev/null
