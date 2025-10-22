package com.norkut.orkesta_mobile.repository.service

import com.norkut.orkesta_mobile.domain.client.Client

object ServiceConstants {


    fun<Api> findApi(api: Class<Api>, isProduction: Boolean): ServiceConfig {
        return if (isProduction) productionApis(api) else devApis(api)
    }


    private fun productionApis(api: Class<*>): ServiceConfig {
        return when (api) {
            ClientService::class.java -> ServiceConfig("7272", "nav-sec-client/")
            DocumentTypeService::class.java -> ServiceConfig("7272", "nav-sec-client/")
            else -> throw ClassNotFoundException(" (Error en el Service Constants)  - Must be a declared class")
        }
    }



    private fun devApis(api: Class<*>): ServiceConfig {
        return when (api) {
            ClientService::class.java -> ServiceConfig("5227")
            DocumentTypeService::class.java -> ServiceConfig("5227")
            else -> throw ClassNotFoundException("(Error en el Service Constants) - Must be a declared class")
        }
    }

}

data class ServiceConfig(
    val port: String,
    val suffix: String = ""
)