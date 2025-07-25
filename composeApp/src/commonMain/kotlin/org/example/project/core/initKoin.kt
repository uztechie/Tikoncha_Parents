package org.example.project.core

import org.example.project.platform.targetModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this)
       modules(
           sharedModule
       )
        modules(targetModule)
    }


}