package com.example.okhttpfornetworkcallinbackgroundthreadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var mTextViewResult : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextViewResult = findViewById(R.id.text_view_result)
        val client : OkHttpClient = OkHttpClient()
        val url : String = "https://reqres.in/api/users?page=2"
        val request : Request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
               e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val myResponse : String? = response.body()?.string()
                    // this@MainActivity 안에 있는 함수를 실행시킬건데 그 함수 이름은 runOnUiThread이다.
                    // 이 함수는 UiThread안에 있으면 바로 실행이될거고 그렇지 않으면 최대한 빨리 실행이 되게 큐에 넣어준다.
                    // 그렇게 하도록 돌려주는 함수이다. 그러니깐 이 함수를 실행하면서 인터페이스를 넘기면 그 함수의 내부에서
                    // 해당 인터페이스의 주소를 지역변수에 가지고 있을거고 그리고 어떤 스위치 같은게 있어서 그때부터 이 인터페이스를
                    // 실행하도록 해준다는거지...
                    this@MainActivity.runOnUiThread(object : Runnable {
                        override fun run() {
                            mTextViewResult.text = myResponse
                        }
                    })
                }
            }
        })
    }
}