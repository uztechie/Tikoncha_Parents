package uz.tikoncha_parent.platform

interface KmpLogger{
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}

expect object Logger: KmpLogger{
    override fun d(tag: String, message: String)
    override fun e(tag: String, message: String, throwable: Throwable?)
}