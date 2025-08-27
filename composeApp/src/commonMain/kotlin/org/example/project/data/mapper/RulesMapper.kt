package org.example.project.data.mapper

import org.example.project.data.remote.model.GetRulesApps
import org.example.project.data.remote.model.GetRulesResponse

fun GetRulesResponse.toRulesMap(): Map<String, Boolean> {
    val apps: GetRulesApps = this.data?.apps ?: return emptyMap()

    val allowSet = apps.allow.map { it.value }.toSet()
    val denySet  = apps.deny.map { it.value }.toSet()

    // allow > deny (istagingizga ko‘ra o‘zgartiring)
    val allPackages = allowSet + denySet
    return allPackages.associateWith { pkg ->
        when {
            pkg in allowSet -> true
            pkg in denySet  -> false
            else -> false // bu hol bo‘lmaydi, allPackages ichida bo‘lsa kamida bittasidadir
        }
    }
}