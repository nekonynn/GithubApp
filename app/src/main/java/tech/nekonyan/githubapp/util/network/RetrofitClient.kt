package tech.nekonyan.githubapp.util.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.nekonyan.githubapp.BuildConfig
import tech.nekonyan.githubapp.util.Constants

class RetrofitClient {
    companion object {
        private val client = OkHttpClient.Builder().run {
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            addInterceptor(interceptor)
            addInterceptor(Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "token " + BuildConfig.API_KEY)
                    .build()
                chain.proceed(newRequest)
            })
            build()
        }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }
}