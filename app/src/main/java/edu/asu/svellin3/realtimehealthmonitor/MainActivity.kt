package edu.asu.svellin3.realtimehealthmonitor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import edu.asu.svellin3.realtimehealthmonitor.helper.RespiratoryDataProcessor
import edu.asu.svellin3.realtimehealthmonitor.sharedPreference.SymptomsDataSharedPreference
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream

class MainActivity : ComponentActivity() {

    private lateinit var heartRateMeasurement: MutableLiveData<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addObserverToLiveData()

        onRespiratoryButtonClick()

        onHeartRateButtonClick()
    }

    private fun addObserverToLiveData() {
        var symptomsData = SymptomsDataSharedPreference.getRespiratoryAndHeartRateValue(this)
        heartRateMeasurement = MutableLiveData(symptomsData.heartRateValue.toInt())
        heartRateMeasurement.observe(this) { result ->
            var tvHeartRate = findViewById<TextView>(R.id.tvHeartRate)
            tvHeartRate.text = "Heart Rate Value: $result"
            SymptomsDataSharedPreference.saveData(this, "heartRateValue", result.toFloat())
        }
    }

    override fun onResume() {
        super.onResume()

        var symptomData = SymptomsDataSharedPreference.getRespiratoryAndHeartRateValue(this)
        setTextInTextViewRespiratory(symptomData.respiratoryValue)
        heartRateMeasurement.postValue(symptomData.heartRateValue.toInt())
    }

    private fun onHeartRateButtonClick() {
        val btnHeartRate = findViewById<Button>(R.id.btnHeartRate)
        btnHeartRate.setOnClickListener {
            getContent.launch("video/*")
        }
    }

    private fun onRespiratoryButtonClick() {
        val btnRespiratory = findViewById<Button>(R.id.btnRespiratory)
        btnRespiratory.setOnClickListener {
            openCSVFilePicker()
        }
    }

    private fun openCSVFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        filePicker.launch(intent)
    }

    fun onSymptomButtonClick(view: View) {
        val intent = Intent(this, SymptomActivity::class.java)
        startActivity(intent)
    }

    private val filePicker: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val activityResultData: Intent? = result.data
                activityResultData?.data?.let { uri ->
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    val respiratoryValue = RespiratoryDataProcessor.processRespiratoryData(
                        inputStream)

                    setTextInTextViewRespiratory(respiratoryValue.toFloat())
                    SymptomsDataSharedPreference.saveData(this, "respiratoryValue",
                        respiratoryValue.toFloat()
                    )
                }
            }
        }

    private fun setTextInTextViewRespiratory(respiratoryValue: Float) {
        var tvRespiratory: TextView = findViewById(R.id.tvRespiratory)
        tvRespiratory.text = "Respiratory value: $respiratoryValue"
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri != null){
            GlobalScope.launch {
                val result = computeHeartRate(uri)
                heartRateMeasurement.postValue(result);
            }
        }else{
            Toast.makeText(this, "No file has been selected", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun computeHeartRate(videoUri: Uri?): Int {

        val retriever = MediaMetadataRetriever()
        var frameList = ArrayList<Bitmap>()
        try {
            retriever.setDataSource(this, videoUri)

            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                    ?: 0
            var aduration = duration!!.toInt()
            var i = 10
            while (i < aduration) {
                val bitmap = retriever.getFrameAtIndex(i)
                frameList.add(bitmap!!)
                i += 5
            }
        } catch (m_e: Exception) {
        } finally {
            retriever?.release()
            var redBucket: Long = 0
            var pixelCount: Long = 0
            val a = mutableListOf<Long>()
            for (i in frameList) {
                redBucket = 0
                for (y in 150 until 250) {
                    for (x in 150 until 250) {
                        val c: Int = i.getPixel(x, y)
                        pixelCount++
                        redBucket += Color.red(c) + Color.blue(c) + Color.green(c)
                    }
                }
                a.add(redBucket)
            }
            val b = mutableListOf<Long>()
            for (i in 0 until a.lastIndex - 5) {
                var temp =
                    (a.elementAt(i) + a.elementAt(i + 1) + a.elementAt(i + 2) + a.elementAt(
                        i + 3
                    ) + a.elementAt(
                        i + 4
                    )) / 4
                b.add(temp)
            }
            var x = b.elementAt(0)
            var count = 0
            for (i in 1 until b.lastIndex) {
                var p = b.elementAt(i.toInt())
                val dif = p - x;
                if ((p - x) > 500) {
                    count = count + 1
                }
                x = b.elementAt(i.toInt())
            }
            var rate = ((count.toFloat() / 45) * 60).toInt()
            val temp = (rate / 2);
            return temp

        }
    }

}
