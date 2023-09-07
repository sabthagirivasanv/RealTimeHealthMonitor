package edu.asu.svellin3.realtimehealthmonitor

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import edu.asu.svellin3.realtimehealthmonitor.entity.KeyValueStore
import edu.asu.svellin3.realtimehealthmonitor.viewModel.KeyValueStoreViewModel

class SymptomActivity : ComponentActivity() {

    private lateinit var keyValueStoreViewModel: KeyValueStoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom)

        keyValueStoreViewModel = ViewModelProvider(this).get(KeyValueStoreViewModel::class.java)

        // get reference to button
        val saveButton = findViewById<Button>(R.id.btnSave)
        val loadButton = findViewById<Button>(R.id.btnLoad)

        saveButton.setOnClickListener {
            insertDataToDatabase()
        }

        loadButton.setOnClickListener {
            Toast.makeText(this@SymptomActivity, "You clicked load button.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun insertDataToDatabase() {
        val etKey = findViewById<EditText>(R.id.etKey)
        val etValue = findViewById<EditText>(R.id.etValue)
        if(!TextUtils.isEmpty(etKey.text) && !TextUtils.isEmpty(etValue.text)){
            val key: String = etKey.text.toString()
            val value: String = etValue.text.toString()

            val newEntry = KeyValueStore(0, key, value)
            keyValueStoreViewModel.addKeyValueStore(newEntry)
            Toast.makeText(this, "successfully added", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Please fill all entries", Toast.LENGTH_SHORT).show()
        }
    }
}