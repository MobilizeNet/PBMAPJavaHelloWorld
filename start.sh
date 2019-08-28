
cp catalina.properties /opt/tomcat/conf
cp connection.properties /opt/tomcat/conf
cp webMAP.properties /opt/tomcat/conf/webMap.properties
cp ReferenceApplication/target/ws-1.0.0.war /opt/tomcat/webapps/ROOT.war
# run tomcat
/opt/tomcat/bin/catalina.sh start