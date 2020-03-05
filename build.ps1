# Function Declarations 

function GetProxyEnabledWebClient
{
    $wc = New-Object System.Net.WebClient
    $proxy = [System.Net.WebRequest]::GetSystemWebProxy()
    $proxy.Credentials = [System.Net.CredentialCache]::DefaultCredentials        
    $wc.Proxy = $proxy
    return $wc
}

function get_package_name([String]$javahome) {
    if ((Test-Path "$(Split-Path $javahome)\jre") -or (Test-Path "$javahome\jre")) {
        $realhome = Split-Path $javahome
        if (((Test-Path "$realhome\src.zip") -or (Test-Path "$realhome\db")) -and (Test-Path "$realhome\bin\java.exe")) {
            return "JDK"
        }
        elseif ((Test-Path "$realhome\lib\tools.jar") -and (Test-Path "$realhome\bin\java.exe")) {
            return "Server-JRE"
        }
    }
    return "JRE"
}

function java_properties([String]$java) {
    $file = New-TemporaryFile
    Write-Verbose "Getting properties by executing ""$java"""
    If (-not (Test-Path $java)) {
        Write-Verbose "Could not find ""$java"""
        return
    }
    [Void](Start-Process $java -NoNewWindow -Wait -ErrorAction Stop -ArgumentList "-XshowSettings:properties -version" -RedirectStandardError $file.FullName)
    [System.Collections.SortedList]$properties = [Hashtable]@{}
    foreach ($line in Get-Content $file.FullName) {
        Write-Debug $line
        if ($line -match '[\s]{4}(?<property>[\w.]+)\s=\s(?<value>.*)') {
            $properties.Add($Matches["property"], $Matches["value"])
            $last = $Matches["property"]
        }
        elseif (($line -match "[\s]{8}(?<value>.*)") -or ((($last -eq "java.vm.info") -or ($last -eq "java.fullversion")) -and ($line -match "(?<value>.*)"))) {
            $value = $properties.Get_Item($last)
            if ($value -is [String]) {
                $properties.Set_Item($last, [Array]@( $value ))
            }
            if ($value -is [Array]) {
                $properties.$last += $Matches["value"]
            }
        }
        else {
            Write-Debug "Not found: $line"
        }
    }
    [Void](Remove-Item $file)
    $properties.Add("custom.package.name", $(get_package_name $properties."java.home" ))
    $jrehome = $javahome = $properties."java.home"
    if ($properties."custom.package.name" -ne "JRE") {
        $javahome = Split-Path $jrehome
        if (-not (Test-Path "$javahome\bin\java.exe")) {
            $javahome = $jrehome
        }
    }
    $properties.Add("custom.java.home", $javahome)
    $properties.Add("custom.jre.home", $jrehome)
    $properties.Add("custom.java.path", $java)
    $properties.Add("custom.vendor", "Unknown")
    if (($properties."java.vendor" -eq "Oracle Corporation") -and ($properties."java.vm.name" -notlike "OpenJDK*")) {
        $properties.Set_Item("custom.vendor", "Oracle")
    }
    if (($properties."java.vendor" -eq "Oracle Corporation") -and ($properties."java.vm.name" -like "OpenJDK*")) {
        $properties.Set_Item("custom.vendor", "OpenJDK")
    }
    if ($properties."java.vendor" -eq "IBM Corporation") {
        $properties.Set_Item("custom.vendor", "IBM")
    }
    if ($properties."java.vendor" -eq "Azul Systems, Inc.") {
        $properties.Set_Item("custom.vendor", "Zulu")
    }
    Write-Verbose "Found Java: $($properties.'custom.vendor') $($properties.'custom.package.name')"
    return $properties
}

function java_from_full_path {
    Write-Host "Searching PATH"
    $javas = [Array]@()
    foreach ($path in $env:PATH.split(";")) {
        If (Test-Path "$path\java.exe") {
            Write-Verbose "Checking for Java in ""$path"""
            $javas += $(java_properties "$path\java.exe")
        }
    }
    if ($javas.Length -eq 0) { Write-Host "No Java found in PATH" }
    return $javas
}

## Start of building script


# Check Java Installation
java_from_full_path

Write-Host "Verify that your are using JDK not JRE"

$VerbosePreference = "continue"

if(!$PSScriptRoot){
    $PSScriptRoot = Split-Path $MyInvocation.MyCommand.Path -Parent
}

[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12;
$MAVEN_VERSION = "3.6.3"
$HELPERS_VERSION = "2.1.0"
$TOOLS_DIR = Join-Path $PSScriptRoot "tools"
$TOMCAT_VER = "8.5.43";
$TOMCAT = Join-Path $TOOLS_DIR "apache-tomcat-$TOMCAT_VER"
$TOMCAT_URL = "https://archive.apache.org/dist/tomcat/tomcat-8/v$TOMCAT_VER/bin/apache-tomcat-$TOMCAT_VER.zip"
$MAVEN_URL = "https://downloads.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip";
$HELPERS = "https://www.myget.org/F/mobilizewebmapjava/maven/com.mobilize/helpers/$HELPERS_VERSION/helpers-$HELPERS_VERSION.jar";
$HELPERS_JAR = "helpers-$HELPERS_VERSION.jar"
$HELPERS_PATH = "$PSScriptRoot/tools/$HELPERS_JAR"
$JAR = Join-Path $Env:JAVA_HOME "\bin\jar"

$MAVEN = Join-Path $TOOLS_DIR "apache-maven-$MAVEN_VERSION"

Write-Host "Checking for tools"
Write-Host "It will check for"
Write-Host "* tools directory"
Write-Host "* maven tools           installation"
Write-Host "* apache tomcat         installation"
Write-Host "* mobilize java helpers installation"
Write-Host "***************************"


# The script will make
# sure tools folder exists.
# This directory will hold any dependencies needed to build the 
# application code.
# For example:
# * apache-maven
# * apache-tomcat
# * mobilize powerbuilder helpers
if ((Test-Path $PSScriptRoot) -and !(Test-Path $TOOLS_DIR)) {
    Write-Host "Creating tools directory..."
    New-Item -Path $TOOLS_DIR -Type directory | out-null
}

# The script will check if the Mobilize.Net Helpers are installed
# if they are not it will download the helpers from a remote repository
Write-Host "Checking for helpers $HELPERS_VERSION"
cd "$PSScriptRoot\tools"
$extractcommand = "$JAR xf $HELPERS_JAR META-INF/maven/com.mobilize/helpers/pom.xml"
if (!(Test-Path $HELPERS_PATH)) {
   try {
        $wc = GetProxyEnabledWebClient
        $wc.DownloadFile($HELPERS, $HELPERS_PATH)
        Write-Host "Extracting Helpers POM to install JAR into Maven Repo"
        cd "$PSScriptRoot\tools"

    } catch {
        $ErrorMessage = $_.Exception.Message
        Throw "Could not download Helpers or error extracting jar $ErrorMessage "
    }
}

# Extract Metada Files that will be use to install helpers
$extractcommand = "$JAR xf helpers-$HELPERS_VERSION.jar META-INF/maven/com.mobilize/helpers/pom.xml"
iex $extractcommand
$extractcommand = "$JAR xf helpers-$HELPERS_VERSION.jar META-INF/maven/com.mobilize/helpers/pom.properties"
iex $extractcommand

Write-Host "Checking for Tomcat $TOMCAT_VER"

# Try download Tomcat if not exists
if (!(Test-Path $TOMCAT)) {
    Write-Verbose -Message "Downloading Tomcat..."
    try {
        $wc = GetProxyEnabledWebClient
        $wc.DownloadFile($TOMCAT_URL, "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER.zip")
        Expand-Archive -Path "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER.zip" -DestinationPath "$TOOLS_DIR" -Force
        Remove-Item -Recurse -Force "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\webapps"
        New-Item -Path "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\webapps" -Type directory | out-null
    } catch {
        $ErrorMessage = $_.Exception.Message
        Throw "Could not download Tomcat $ErrorMessage "
    }
}
$Env:CATALINA_BASE = "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER"
$Env:CATALINA_HOME = "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER"
$Env:TOMCAT_HOME = "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER"
$WEBAPPS =  "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\webapps"
if (!(Test-Path $WEBAPPS)) {
    New-Item -Path $WEBAPPS -Type directory | out-null
}

$Env:Path += ";$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\bin";


Write-Host "Checking for Maven $MAVEN_VERSION"
# Try download Maven if not exists
if (!(Test-Path $MAVEN)) {
    Write-Verbose -Message "Downloading Maven..."
    try {
        $wc = GetProxyEnabledWebClient
        $wc.DownloadFile($MAVEN_URL, "$TOOLS_DIR\apache-maven-$MAVEN_VERSION-bin.zip")
        Expand-Archive -Path "$TOOLS_DIR\apache-maven-$MAVEN_VERSION-bin.zip" -DestinationPath "$TOOLS_DIR" -Force
    } catch {
        $ErrorMessage = $_.Exception.Message
        Throw "Could not download Maven $ErrorMessage "
    }
}
# Adding maven to PATH
$Env:Path += ";$TOOLS_DIR\apache-maven-$MAVEN_VERSION\bin";

Write-Host "Starting compilation"
Write-Host "============================"



Write-Host "Building Migrated Application Code"
Set-Location -Path "$PSScriptRoot\Target"

# The helpers jar must be installed into the repository
Write-Host "Installing Helpers $HELPERS_VERSION"
Write-Host "mvn install:install-file -Dfile=$HELPERS_PATH -DgroupId=com.mobilize -DartifactId=helpers -Dversion=$HELPERS_VERSION -Dpackaging=jar -DpomFile=$TOOLS_DIR\META-INF\maven\com.mobilize\helpers\pom.xml"
mvn install:install-file -Dfile="$HELPERS_PATH" -DgroupId="com.mobilize" -DartifactId="helpers" -Dversion="$HELPERS_VERSION" -Dpackaging=jar -DpomFile="$TOOLS_DIR\META-INF\maven\com.mobilize\helpers\pom.xml"

Write-Host "Building Code"
mvn.cmd clean install 
Set-Location -Path "$PSScriptRoot"

Write-Host "Building WAR"
Set-Location -Path "$PSScriptRoot\ReferenceApplication"
mvn.cmd clean install 
Set-Location -Path "$PSScriptRoot"

Write-Host "Done building WAR"

Write-Host "Copying property files"
Write-Host "**********************************"

Write-Host "Copying properties files to $TOOLS_DIR\apache-tomcat-$TOMCAT_VER\conf"
copy "$PSScriptRoot\catalina.properties" "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\conf"
copy "$PSScriptRoot\connection.properties" "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\conf"
copy "$PSScriptRoot\webMAP.properties" "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\conf"
copy "$PSScriptRoot\ReferenceApplication\target\ws-1.0.0.war" "$TOOLS_DIR\apache-tomcat-$TOMCAT_VER\webapps\ROOT.war"

Write-Host "The application is ready. You can use catalina run or catalina start"