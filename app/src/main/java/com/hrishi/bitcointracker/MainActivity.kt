package com.hrishi.bitcointracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currencies=resources.getStringArray(R.array.currency_array)
        val spinner=findViewById<Spinner>(R.id.spinner_view)
        if(spinner!=null){
            val adapter=ArrayAdapter(this,R.layout.spinner_item,currencies)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.adapter=adapter
            spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val base_url="http://api.coinlayer.com/api/live?access_key="
                    val key="c22b6b642499bad6ff68cc643c92d4ff"
                    val url=base_url+key+"&TARGET="+currencies[position]+"&symbols=BTC"
                    result(url)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }
//    fun getResult(url:String){
//        Log.e("msg",url)
//        val queue=Volley.newRequestQueue(this)
//        val stringRequest= StringRequest(Request.Method.GET,url,
//            {response ->
//                Log.e("msg",response.toString())
//            }, {
//                it->
//                Log.e("error1",it.toString());
//            })
//        queue.add(stringRequest)
//    }
    fun result(url: String) {
        val txt=findViewById<TextView>(R.id.mainText)
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>?,
                response: JSONObject?
            ) {
                Log.d("response", "JSON: $response")
                try {
                    val price:JSONObject? = response?.getJSONObject("rates")
                    val reqRate = price?.getString("BTC")
                    txt.text = reqRate
                } catch (E: JSONException) {
                    E.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>?,
                e: Throwable?,
                response: JSONObject?
            ) {
                Log.d("Status", "Request fail! Status code: $statusCode")
                Log.d("Response", "Fail response: $response")
                Log.e("error", e.toString())
            }
        })
    }
}