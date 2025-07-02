package org.example.project.domain

import org.example.project.data.remote.TikonchaApi

class TikonchaRepositoryImpl(private val api: TikonchaApi): TikonchaRepository {

    override suspend fun login(): List<String> {
        return api.login()
    }


}