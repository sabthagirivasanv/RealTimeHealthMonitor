package edu.asu.svellin3.realtimehealthmonitor

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onSymptomButtonClick(view: View) {
        val intent = Intent(this, SymptomActivity::class.java)
        startActivity(intent)
    }
}