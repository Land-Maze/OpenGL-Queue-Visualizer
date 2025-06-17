#!/bin/bash

# This script doesn't check the root directory, so it should be run from the project root.

set -e

OS="$(uname -s | tr '[:upper:]' '[:lower:]')"

case "$OS" in
  linux*) NATIVE_PATH="natives/linux";;
  darwin*) NATIVE_PATH="natives/macos";;
  msys*|mingw*|cygwin*) NATIVE_PATH="natives/windows";;
  *) echo "[ERROR] Unsupported OS: $OS"; exit 1;;
esac

echo "[INFO] Using native path: $NATIVE_PATH"

MAIN_CLASS="stackvisualizer.Main"

mvn clean compile

echo "[INFO] Running with natives from: $NATIVE_PATH"
mvn exec:java \
  -Dexec.mainClass="$MAIN_CLASS" \
  -Dexec.args="" \
  -Dexec.cleanupDaemonThreads=false \
  -Dexec.jvmArgs="-Djava.library.path=$NATIVE_PATH"