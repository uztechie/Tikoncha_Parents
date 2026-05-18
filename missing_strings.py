import xml.etree.ElementTree as ET
from pathlib import Path

BASE = "./composeApp/src/commonMain/composeResources"
DEFAULT = f"{BASE}/values/strings.xml"

TARGETS = {
    "ru": f"{BASE}/values-ru/strings.xml",
}

def load_strings(path):
    tree = ET.parse(path)
    root = tree.getroot()
    return {el.get("name"): el for el in root.findall("string")}

print(f"[INFO] Default: {DEFAULT}")
default_map = load_strings(DEFAULT)
print(f"[INFO] Default'da {len(default_map)} ta string\n")

for lang, path in TARGETS.items():
    exists = Path(path).exists()

    if exists:
        target_map = load_strings(path)
        print(f"[INFO] {lang}: fayl bor, {len(target_map)} ta string mavjud")
    else:
        target_map = {}
        print(f"[INFO] {lang}: FAYL YO'Q — barcha stringlarni yangi faylga yozish kerak")

    missing = []
    skipped = 0
    for name, el in default_map.items():
        if el.get("translatable") == "false":
            skipped += 1
            continue
        if name not in target_map:
            missing.append(name)

    print(f"[INFO] translatable=false: {skipped} ta o'tkazildi")
    print(f"[INFO] Yetishmayotgan: {len(missing)} ta\n")

    if not missing:
        print(f"<!-- {lang.upper()}: Hammasi to'liq, yetishmayotgan string yo'q -->\n")
        continue

    print(f"<!-- ========== {lang.upper()} uchun yetishmayotgan {len(missing)} ta string ========== -->")
    print(f"<!-- Bularni {path} faylingizga qo'shing va qiymatlarini tarjima qiling -->\n")
    for name in missing:
        el = default_map[name]
        print(ET.tostring(el, encoding="unicode").strip())
    print()