#./bin/bash
PSScriptRoot="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
TOOLS_DIR="${PSScriptRoot}/tools"
TOMCAT_VER="8.5.43"
TOMCAT="${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}"
TOMCAT_URL="https://archive.apache.org/dist/tomcat/tomcat-8/v$TOMCAT_VER/bin/apache-tomcat-${TOMCAT_VER}.zip"
MAVEN_URL="https://www-us.apache.org/dist/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.zip"
HELPERS="https://www.myget.org/F/mobilizewebmapjava/maven/com.mobilize/helpers/2.1.0/helpers-2.1.0.jar"
HELPERS_JAR="helpers-2.1.0.jar"
HELPERS_PATH="${PSScriptRoot}/tools/${HELPERS_JAR}"
MAVEN="${TOOLS_DIR}/apache-maven-3.6.1"
echo "Checking for tools"
echo "***************************"
echo "${TOOLS_DIR}"
echo "***************************"
# Make sure tools folder exists
if [ ! -d "$TOOLS_DIR" ]; then
    echo "Creating tools directory..."
    mkdir "$TOOLS_DIR"
fi
echo  "Checking for helpers"
echo "***************************"
if [ ! -d "$HELPERS_PATH" ]; then
    wget "$HELPERS" -O "$HELPERS_PATH"
fi    
echo  "Checking for Tomcat"
echo "***************************"
# Try download Tomcat if not exists
if [ ! -d "$TOMCAT" ]; then
    echo "Downloading Tomcat..."
    echo "***************************"
    wget "$TOMCAT_URL" -O "$TOOLS_DIR/apache-tomcat-$TOMCAT_VER.zip"
    cd "$TOOLS_DIR"
    unzip "$TOOLS_DIR/apache-tomcat-$TOMCAT_VER.zip"
    # Remove sample apps
    rm -rf "$TOOLS_DIR/apache-tomcat-$TOMCAT_VER/webapps"
    # create empty webapps folder
    mkdir "$TOOLS_DIR/apache-tomcat-$TOMCAT_VER/webapps"
fi    
export CATALINA_BASE="${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}"
export CATALINA_HOME="${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}"
export TOMCAT_HOME="${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}"
WEBAPPS="${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}/webapps"

#make sure that the webapps dir exists
if [ ! -d "$WEBAPPS" ]; then
    mkdir "$WEBAPPS"
fi

export PATH="$TOOLS_DIR/apache-tomcat-$TOMCAT_VER/bin:${PATH}"
echo  "Checking for Maven"
echo "***************************"
# Try download Maven if not exists
if [ ! -d "$MAVEN" ]; then
    echo "Downloading Maven..."
    echo "***************************"
    wget "$MAVEN_URL" -O "${TOOLS_DIR}/apache-maven-3.6.1-bin.zip"
fi

export PATH="${TOOLS_DIR}/apache-maven-3.6.1/bin:${PATH}"
echo  "Starting compilation"
echo  "============================"
echo  "Building Migrated Application Code"
echo  "============================"
cd "$PSScriptRoot/Target"
echo  "Installing Helpers"
mvn install:install-file -Dfile="$HELPERS_PATH" -DgroupId="com.mobilize" -DartifactId="helpers" -Dversion="2.1.0" -Dpackaging=jar -DpomFile="$TOOLS_DIR/META-INF/maven/com.mobilize/helpers/pom.xml"
echo  "Building Code"
echo  "============================"
mvn clean install 
cd "$PSScriptRoot"
echo  "Building WAR"
echo  "============================"
cd "${PSScriptRoot}/ReferenceApplication"
mvn clean install 
cd "$PSScriptRoot"
echo  "Done building WAR"
echo  "============================"
echo  "Copying property files"
echo  "**********************************"
echo  "Copying properties files to ${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}/conf"
cp "$PSScriptRoot/catalina.properties" "${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}/conf"
cp "$PSScriptRoot/connection.properties" "${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}/conf"
cp "$PSScriptRoot/webMAP.properties" "${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}/conf"
cp "$PSScriptRoot/ReferenceApplication/target/ws-1.0.0.war" "${TOOLS_DIR}/apache-tomcat-${TOMCAT_VER}/webapps/ROOT.war"
echo "The application is ready. You can use catalina run or catalina start"