package uz.tikoncha_parent.presentation.chat.model

enum class DeliveryStatus {
    SENDING,  // local optimistic
    SENT,     // serverga saqlandi (id bor)
    READ,     // is_read=true yoki ReadUpdate event
    FAILED    // API error / timeout
}