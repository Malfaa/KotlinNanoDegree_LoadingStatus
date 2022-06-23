package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

        file_result.text = intent.getStringExtra("fileName")
        status_result.text = intent.getStringExtra("status")

        if (status_result.text == "Success") status_result.setTextColor(getColor(R.color.green))
        else status_result.setTextColor(getColor(R.color.red))

        ok_button.setOnClickListener {
            finish()
        }
    }

}
