package com.elyeproj.networkexperiment

import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class Repository {

    @Inject
    lateinit var httpClient: OkHttpClient

    private var disposable = Disposables.disposed()

    private val httpUrlBuilder = HttpUrl.Builder()
        .scheme("https")
        .host("en.wikipedia.org")
        .addPathSegment("w")
        .addPathSegment("api.php")
        .addQueryParameter("action", "query")
        .addQueryParameter("format", "json")
        .addQueryParameter("list", "search")


    fun performFetch(searchText: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        disposable.dispose()
        disposable = Single.just(searchText)
            .map{fetchOnBackground(it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) })
    }

    private fun fetchOnBackground(searchText: String): String {
        val httpUrl = httpUrlBuilder.addQueryParameter("srsearch", searchText)
            .build()
        val request = Request.Builder().get().url(httpUrl).build()
        val response = httpClient.newCall(request).execute()

        return if (response.isSuccessful) {
            val raw = response.body()?.string()
            try {
                val result = Gson().fromJson(raw, Model.Result::class.java)
                result.query.searchinfo.totalhits.toString()
            } catch (exception: JsonSyntaxException) {
                "No data found "
            }
        } else {
            response.message()
        }
    }

    fun cancel() {
        disposable.dispose()
    }
}