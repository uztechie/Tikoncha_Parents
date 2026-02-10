import xml.etree.ElementTree as ET

def read_keys(path):
    tree = ET.parse(path)
    root = tree.getroot()
    return {child.attrib['name'] for child in root if child.tag == "string"}

default_keys = read_keys(
    "composeApp/src/commonMain/composeResources/values/strings.xml"
)

ru_keys = read_keys(
    "composeApp/src/commonMain/composeResources/values-ru/strings.xml"
)

missing = default_keys - ru_keys

print("\nMissing in RU:\n")
for key in sorted(missing):
    print(key)

print(f"\nTotal missing: {len(missing)}")