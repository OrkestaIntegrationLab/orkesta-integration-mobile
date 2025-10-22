package com.norkut.orkesta_mobile.common.utils

class Environment {

    val urlBase =  "http://10.0.2.2"

    var isProduction = false
        private set

    var remoteUrl = urlBase
        private set

    var localUrl = urlBase
        private set

    fun host(url: String) = url.replace("http://", String()).replace("https://", String())

}