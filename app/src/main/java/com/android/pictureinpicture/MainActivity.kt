package com.android.pictureinpicture

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by tfakioglu on 13.10.2022.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        findViewById<Button>(R.id.button)
            .setOnClickListener {
                val intent = Intent(this, VideoActivity::class.java)
                startActivity(intent)
            }
    }
}