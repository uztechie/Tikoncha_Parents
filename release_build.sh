#!/usr/bin/env bash
set -euo pipefail

# =========================
# CONFIG
# =========================
BUILD_FILE="composeApp/build.gradle.kts"
RELEASE_DIR="release"                 # rootdagi /release
LOCAL_PROPERTIES="local.properties"   # rootdagi local.properties

# local.properties ichidagi kalitlar:
LP_TOKEN_KEY="SLACK_BOT_TOKEN"
LP_CHANNEL_KEY="SLACK_CHANNEL_ID"

# =========================
# HELPERS
# =========================
die() { echo "❌ $1" >&2; exit 1; }

read_local_prop() {
  local key="$1"
  [[ -f "$LOCAL_PROPERTIES" ]] || return 0
  # key=value format (spacesiz). value ichida '=' bo‘lsa ham ushlaydi.
  grep -E "^${key}=" "$LOCAL_PROPERTIES" | head -n 1 | cut -d'=' -f2- | tr -d '\r' || true
}

# =========================
# PRECHECKS
# =========================
[[ -f "$BUILD_FILE" ]] || die "File not found: $BUILD_FILE"

# =========================
# SLACK CREDS (env > local.properties)
# =========================
SLACK_BOT_TOKEN="${SLACK_BOT_TOKEN:-$(read_local_prop "$LP_TOKEN_KEY")}"
SLACK_CHANNEL_ID="${SLACK_CHANNEL_ID:-$(read_local_prop "$LP_CHANNEL_KEY")}"

[[ -n "${SLACK_BOT_TOKEN}" ]] || die "SLACK_BOT_TOKEN topilmadi. local.properties ga ${LP_TOKEN_KEY}=xoxb-... qo‘ying yoki env orqali bering."
[[ -n "${SLACK_CHANNEL_ID}" ]] || die "SLACK_CHANNEL_ID topilmadi. local.properties ga ${LP_CHANNEL_KEY}=C... qo‘ying yoki env orqali bering."

# =========================
# 1) VERSION BUMP (versionCode +1, versionName patch +1)
# =========================
echo "🔎 Reading + bumping versions in $BUILD_FILE ..."

python3 - <<'PY'
import re, pathlib, sys

p = pathlib.Path("composeApp/build.gradle.kts")
t = p.read_text(encoding="utf-8")

m_code = re.search(r'versionCode\s*=\s*(\d+)', t)
m_name = re.search(r'versionName\s*=\s*"([^"]+)"', t)

if not m_code or not m_name:
    print("❌ versionCode/versionName topilmadi. build.gradle.kts formatini tekshiring.")
    sys.exit(1)

current_code = int(m_code.group(1))
current_name = m_name.group(1)

parts = current_name.split(".")
while len(parts) < 3:
    parts.append("0")

try:
    major, minor, patch = (int(parts[0] or 0), int(parts[1] or 0), int(parts[2] or 0))
except ValueError:
    print(f'❌ versionName "{current_name}" semver (x.y.z) emas. Iltimos shunday format qiling.')
    sys.exit(1)

new_code = current_code + 1
new_name = f"{major}.{minor}.{patch+1}"

print(f"Current versionCode: {current_code}")
print(f"Current versionName: {current_name}")
print(f"New     versionCode: {new_code}")
print(f"New     versionName: {new_name}")

# backup
bak = p.with_suffix(p.suffix + ".bak")
bak.write_text(t, encoding="utf-8")

# replace only first occurrence
t2 = re.sub(r'(versionCode\s*=\s*)\d+', r'\g<1>'+str(new_code), t, count=1)
t2 = re.sub(r'(versionName\s*=\s*")([^"]+)(")', r'\g<1>'+new_name+r'\g<3>', t2, count=1)

p.write_text(t2, encoding="utf-8")
print("✅ Version updated (backup created: composeApp/build.gradle.kts.bak)")
PY

# =========================
# 2) "GRADLE SYNC" (CLI tomonda config refresh)
# =========================
echo "🔄 Gradle config refresh (CLI) ..."
./gradlew --stop >/dev/null 2>&1 || true
./gradlew :composeApp:tasks -q >/dev/null

# =========================
# 3) BUILD SIGNED RELEASE APK + AAB
# =========================
echo "🧹 Clean + build release APK/AAB ..."
./gradlew :composeApp:clean
./gradlew :composeApp:assembleRelease
./gradlew :composeApp:bundleRelease

# =========================
# 4) MOVE OUTPUTS TO /release
# =========================
mkdir -p "$RELEASE_DIR"

APK_PATH="$(find composeApp/build/outputs/apk/release -maxdepth 1 -type f -name "*.apk" | head -n 1 || true)"
AAB_PATH="$(find composeApp/build/outputs/bundle/release -maxdepth 1 -type f -name "*.aab" | head -n 1 || true)"

[[ -n "$APK_PATH" && -f "$APK_PATH" ]] || die "APK not found: composeApp/build/outputs/apk/release/*.apk"
[[ -n "$AAB_PATH" && -f "$AAB_PATH" ]] || die "AAB not found: composeApp/build/outputs/bundle/release/*.aab"

# old release files (optional cleanup): shu ikki turdagi faylni eski release’dan olib tashlaymiz
rm -f "$RELEASE_DIR"/*.apk "$RELEASE_DIR"/*.aab 2>/dev/null || true

mv -f "$APK_PATH" "$RELEASE_DIR/"
mv -f "$AAB_PATH" "$RELEASE_DIR/"

APK_OUT="$RELEASE_DIR/$(basename "$APK_PATH")"
AAB_OUT="$RELEASE_DIR/$(basename "$AAB_PATH")"

echo "📦 Release files ready:"
echo " - $(pwd)/$APK_OUT"
echo " - $(pwd)/$AAB_OUT"

# =========================
# 5) SLACK UPLOAD (NEW FLOW)
# =========================
upload_to_slack () {
  local file_path="$1"
  local title="$2"

  [[ -f "$file_path" ]] || die "File not found: $file_path"

  local filename length
  filename="$(basename "$file_path")"
  length="$(wc -c < "$file_path" | tr -d ' ')"

  echo "☁️  Slack upload init: $filename ($length bytes)"

  # 1) files.getUploadURLExternal
  local resp
  resp="$(curl -sS -X POST "https://slack.com/api/files.getUploadURLExternal" \
    -H "Authorization: Bearer ${SLACK_BOT_TOKEN}" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "filename=${filename}" \
    --data-urlencode "length=${length}")"

  local ok upload_url file_id
  read -r ok upload_url file_id < <(
  printf '%s' "$resp" | python3 - <<'PY'
import json, sys
r = json.load(sys.stdin)
print(str(r.get("ok", False)).lower(), r.get("upload_url",""), r.get("file_id",""))
PY
)


  [[ "$ok" == "true" && -n "$upload_url" && -n "$file_id" ]] || die "Slack getUploadURLExternal failed: $resp"

  # 2) upload bytes
  echo "⬆️  Uploading bytes..."
  curl -sS -X POST "$upload_url" \
    -H "Content-Type: application/octet-stream" \
    --data-binary @"$file_path" >/dev/null

  # 3) completeUploadExternal (share to channel)
  echo "✅ Completing upload..."
  local files_json
  files_json="$(python3 - <<PY
import json
print(json.dumps([{"id":"$file_id","title":"$title"}]))
PY
)"

  local resp2
  resp2="$(curl -sS -X POST "https://slack.com/api/files.completeUploadExternal" \
    -H "Authorization: Bearer ${SLACK_BOT_TOKEN}" \
    -F "files=${files_json}" \
    -F "channel_id=${SLACK_CHANNEL_ID}" \
    -F "initial_comment=${title}")"

  local ok2
ok2="$(printf '%s' "$resp2" | python3 - <<'PY'
import json, sys
r = json.load(sys.stdin)
print(str(r.get("ok", False)).lower())
PY
)"

  [[ "$ok2" == "true" ]] || die "Slack completeUploadExternal failed: $resp2"

  echo "🎉 Uploaded to Slack: $filename"
}

echo "🚀 Uploading APK + AAB to Slack..."
upload_to_slack "$APK_OUT" "Release APK: $(basename "$APK_OUT")"
upload_to_slack "$AAB_OUT" "Release AAB: $(basename "$AAB_OUT")"

echo "✅ DONE"
