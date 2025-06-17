#!/bin/bash
set -e

ARTIFACT_ID="java-visualisation-stack"
VERSION="1.0-SNAPSHOT"
TARGET_JAR="target/${ARTIFACT_ID}-${VERSION}.jar"

NATIVE_GROUP="org/lwjgl"
NATIVE_VERSION="3.3.3"

declare -A PLATFORMS=(
  [windows]="natives-windows"
  [linux]="natives-linux"
  [macos]="natives-macos"
)

echo "[INFO] Building project with Maven..."
mvn clean package

if [ ! -f "$TARGET_JAR" ]; then
  echo "[ERROR] JAR not found: $TARGET_JAR"
  exit 1
fi

echo "[INFO] Preparing natives..."

mkdir -p natives/windows natives/linux natives/macos

function extract_natives {
  local platform=$1
  local dest_dir=$2
  local classifier="natives-$platform"

  echo "[INFO] Extracting natives for $platform..."

  modules=("lwjgl" "lwjgl-glfw" "lwjgl-opengl")

  for mod in "${modules[@]}"; do
    local jar_path="$HOME/.m2/repository/org/lwjgl/$mod/$NATIVE_VERSION/$mod-$NATIVE_VERSION-$classifier.jar"
    if [ ! -f "$jar_path" ]; then
      echo "[ERROR] Native JAR not found: $jar_path"
      exit 1
    fi
    (cd "$dest_dir" && jar xf "$jar_path")
  done
}

extract_natives windows natives/windows
extract_natives linux natives/linux
extract_natives macos natives/macos

echo "[INFO] Creating zip packages..."

for platform in "${!PLATFORMS[@]}"; do
  zip_name="${ARTIFACT_ID}-${platform}.zip"
  natives_dir="natives/$platform"

  echo "[INFO] Packaging $zip_name ..."

  (
    cd "$natives_dir"
    zip -r "../../$zip_name" .
  )
  # Append jar to the zip (without changing zip structure)
  zip -j "$zip_name" "$TARGET_JAR"

done

echo "[INFO] Done."
