package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.model.DeepLink

object DeepLinkBus {
    private val ls = mutableSetOf<DeepLinkListener>()
    fun add(l: DeepLinkListener) { ls += l }
    fun remove(l: DeepLinkListener) { ls -= l }
    fun open(link: DeepLink) { ls.forEach { it.onOpen(link) } }
}

interface DeepLinkListener { fun onOpen(link: DeepLink) }