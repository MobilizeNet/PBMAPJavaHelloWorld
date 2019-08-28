FROM gitpod/workspace-full

USER root

RUN mkdir /opt/tmptomcat/
WORKDIR /opt/tmptomcat/
RUN wget http://mirrors.ucr.ac.cr/apache/tomcat/tomcat-8/v8.5.43/bin/apache-tomcat-8.5.43.zip

RUN unzip apache*.zip
RUN mv /opt/tmptomcat/apache-tomcat-8.5.43 /opt/tomcat
RUN chown -R gitpod: /opt/tomcat
RUN rm -rf /opt/tomcat/webapps
RUN mkdir /opt/tomcat/webapps/
RUN chown -R gitpod: /opt/tomcat/webapps
RUN chmod +x /opt/tomcat/bin/*.sh
ENV TOMCAT_HOME=/opt/tomcat

USER gitpod
EXPOSE 8080
