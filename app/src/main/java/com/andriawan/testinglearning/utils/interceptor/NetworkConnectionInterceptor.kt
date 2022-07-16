package com.andriawan.testinglearning.utils.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(
    private val context: Context
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoNetworkException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private val isConnected: Boolean
        get() {
            val connectionManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectionManager.run {
                    connectionManager.getNetworkCapabilities(connectionManager.activeNetwork)?.run {
                        when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                    || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                    || hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                            -> {
                                return true
                            }

                            else -> {
                                return false
                            }
                        }
                    }
                }
            } else {
                connectionManager.run {
                    connectionManager.activeNetworkInfo?.run {
                        when (type) {
                            ConnectivityManager.TYPE_WIFI,
                            ConnectivityManager.TYPE_MOBILE,
                            ConnectivityManager.TYPE_VPN
                            -> {
                                return true
                            }
                            else -> {
                                return false
                            }
                        }
                    }
                }
            }

            return false
        }


    inner class NoNetworkException : IOException() {
        override val message: String
            get() = "No Network Connection"
    }
}