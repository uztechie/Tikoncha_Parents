#!/usr/bin/env bash
set -euo pipefail

BUILD_FILE="composeApp/build.gradle.kts"
RELEASE_DIR="release"

if [[ ! -f "$BUILD_FILE" ]]; then
  echo "❌ File not found: $BUILD_FILE"
  exit 1
fi

# --- Current versionlarni o‘qish ---
currentCode="$(grep -E 'versionCode[[:space:]]*=' "$BUILD_FILE" | head -n 1 | grep -Eo '[0-9]+')"
currentName="$(grep -E 'versionName[[:space:]]*=' "$BUILD_FILE" | head -n 1 | sed -E 's/.*"([^"]+)".*/\1/')"

echo "Current versionCode: $currentCode"
echo "Current versionName: $currentName"

# --- version bump ---
IFS='.' read -r major minor patch <<< "$currentName"
major="${major:-0}"
minor="${minor:-0}"
patch="${patch:-0}"

newPatch=$((patch + 1))
newName="${major}.${minor}.${newPatch}"
newCode=$((currentCode + 1))

echo "New versionCode: $newCode"
echo "New versionName: $newName"

# backup
cp "$BUILD_FILE" "${BUILD_FILE}.bak"

# replace
sed -i '' -E "0,/versionCode[[:space:]]*=[[:space:]]*${currentCode}/s//versionCode = ${newCode}/" "$BUILD_FILE"
sed -i '' -E "0,/versionName[[:space:]]*=[[:space:]]*\"${currentName}\"/s//versionName = \"${newName}\"/" "$BUILD_FILE"

echo "✅ Version updated"

# --- Build ---
./gradlew :composeApp:assembleRelease
./gradlew :composeApp:bundleRelease

# --- release papka ---
mkdir -p "$RELEASE_DIR"

APK_PATH="$(find composeApp/build/outputs/apk/release -name "*.apk" | head -n 1)"
AAB_PATH="$(find composeApp/build/outputs/bundle/release -name "*.aab" | head -n 1)"

if [[ -f "$APK_PATH" ]]; then
  mv "$APK_PATH" "$RELEASE_DIR/"
  echo "✅ APK moved to $RELEASE_DIR/"
else
  echo "❌ APK not found"
fi

if [[ -f "$AAB_PATH" ]]; then
  mv "$AAB_PATH" "$RELEASE_DIR/"
  echo "✅ AAB moved to $RELEASE_DIR/"
else
  echo "❌ AAB not found"
fi

echo "📦 Release files: $(pwd)/$RELEASE_DIR"
