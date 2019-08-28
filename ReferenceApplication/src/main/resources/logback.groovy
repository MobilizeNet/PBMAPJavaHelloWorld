import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.WARN

def LOG_HOME = System.getenv("LOG_DIR")
def ARCH_HOME = "${LOG_HOME}/archived"

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} >> %-3relative [%thread] %-5level %logger{36} - %msg%n"
    }
}

appender("FILE", RollingFileAppender) {
    file = "${LOG_HOME}/app.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger - %msg%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${ARCH_HOME}/app.%d{yyyy-MM-dd}.%i.log"
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) { maxFileSize = "10MB" }
    }
}

//Jasper logger disabled
logger("net.sf.jasperreports", ERROR, ["CONSOLE"], false)
logger("org.apache.commons.beanutils", ERROR, ["CONSOLE"], false)
logger("org.apache.commons.digester", ERROR, ["CONSOLE"], false)
logger("org.apache.commons.javaflow.bytecode", ERROR, ["CONSOLE"], false)

logger("ch.qos.logback", ERROR, ["CONSOLE"], false)
logger("org.springframework", ERROR, ["FILE", "CONSOLE"], false)
logger("com.mobilize", ERROR, ["FILE", "CONSOLE"], true)
root(ALL, ["FILE", "CONSOLE"])
