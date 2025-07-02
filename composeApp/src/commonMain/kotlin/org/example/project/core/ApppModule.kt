package org.example.project.core

import org.example.project.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    viewModelOf(::HomeViewModel)
}