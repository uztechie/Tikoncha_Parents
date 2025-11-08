package uz.tikoncha_parent.presentation.policy.rule_type_selection

enum class RuleType(val id: String) {
    TIME("time"),
    LOCATION("location"),
    WIFI("wifi"),
    LAUNCH_COUNT("launch_count"),
    USAGE_LIMIT("usage_limit"),
    NONE("")
}