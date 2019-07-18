package com.elyeproj.networkexperiment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.net.ConnectivityManager



class MainActivity : AppCompatActivity() {

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val component = DaggerMainComponent.builder().mainModule(MainModule(this)).build()
        component.inject(repository)
    }

    fun beginSearch(view: View) {
        if (searchText.text.isNotBlank()) {
            resultText.text = ""
            progressIndicator.visibility = View.VISIBLE
            repository.performFetch(searchText.text.toString(),
                {showResult("Count is $it")},
                {showResult(it.localizedMessage)})
        }
    }

    private fun showResult(result: String) {
        progressIndicator.visibility = View.GONE
        resultText.text = result
    }

    override fun onPause() {
        repository.cancel()
        super.onPause()
    }
}
