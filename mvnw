#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper script - downloads Maven if needed and runs the build
# ----------------------------------------------------------------------------

if [ -z "$JAVA_HOME" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
MAVEN_PROJECTBASEDIR="$SCRIPT_DIR"
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Downloading Maven wrapper..."
  mkdir -p "$(dirname "$WRAPPER_JAR")"
  if command -v wget >/dev/null 2>&1; then
    wget -q -O "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
  elif command -v curl >/dev/null 2>&1; then
    curl -sL -o "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
  else
    echo "Error: wget or curl required to download Maven wrapper."
    exit 1
  fi
fi

exec "$JAVA_HOME/bin/java" -classpath "$WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
