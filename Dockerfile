FROM gitpod/workspace-full

USER root


RUN mkdir /opt/tmptomcat/
WORKDIR /opt/tmptomcat/
RUN wget https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.45/bin/apache-tomcat-8.5.45.zip

RUN unzip apache*.zip
RUN mv /opt/tmptomcat/apache-tomcat-8.5.45 /opt/tomcat
RUN chown -R gitpod: /opt/tomcat
RUN rm -rf /opt/tomcat/webapps
RUN mkdir /opt/tomcat/webapps/
RUN chown -R gitpod: /opt/tomcat/webapps
RUN chmod +x /opt/tomcat/bin/*.sh
RUN chmod +x /workspace/PBMAPJavaHelloWorld/*.sh
ENV TOMCAT_HOME=/opt/tomcat

USER gitpod
EXPOSE 8080
