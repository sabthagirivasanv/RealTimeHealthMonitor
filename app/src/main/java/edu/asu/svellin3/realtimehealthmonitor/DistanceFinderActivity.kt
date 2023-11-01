package edu.asu.svellin3.realtimehealthmonitor

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import edu.asu.svellin3.realtimehealthmonitor.service.GoogleMapsService
import edu.asu.svellin3.realtimehealthmonitor.sharedPreference.SymptomsDataSharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.UUID

class DistanceFinderActivity : ComponentActivity() {

    private lateinit var distanceData: MutableLiveData<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distance_finder)
        addObserverToLiveData()
    }

    private fun addObserverToLiveData() {
        distanceData = MutableLiveData("")
        distanceData.observe(this) { result ->
            var tvDistance = findViewById<TextView>(R.id.tvDistance)
            tvDistance.text = "Diff: $result"
        }
    }

    fun onCalculateDistanceButtonClick(view: View) {
        val sourceLatitude = findViewById<EditText>(R.id.etSourceLatitude).text
        val sourceLongitude = findViewById<EditText>(R.id.etSourceLongitude).text
        val destinationLatitude = findViewById<EditText>(R.id.etDestinationLatitude).text
        val destinationLongitude = findViewById<EditText>(R.id.etDestinationLongitude).text

        val source = doubleArrayOf(sourceLatitude.toString().toDouble(), sourceLongitude.toString().toDouble())
        val destination = doubleArrayOf(destinationLatitude.toString().toDouble(), destinationLongitude.toString().toDouble())
        Log.d("DIST", "onCalculateDistanceButtonClick: PRE")
        callDistanceCalculationAsync(source, destination)
        Log.d("DIST", "onCalculateDistanceButtonClick: POST")
    }

    private fun callDistanceCalculationAsync(source: DoubleArray, destination: DoubleArray) {
        lifecycleScope.launch(Dispatchers.IO) {
            val deferredResult = async(Dispatchers.IO) {
                GoogleMapsService.getDistanceMatrixAsync(source, destination)
            }
            val result = deferredResult.await()
            if (result != null) {
                println("Distance Matrix API Response: $result")
                distanceData.postValue(processJSONOutput(result))
            } else {
                println("Error occurred while making the request.")
            }
        }
    }

    private fun processJSONOutput(input: String): String {
        val jsonObject = JSONObject(input)
        val rows = jsonObject.getJSONArray("rows")

        var normalSpeed = 0.0
        var trafficSpeed = 0.0
        if (rows.length() > 0) {
            val elements = rows.getJSONObject(0).getJSONArray("elements")
            if (elements.length() > 0) {
                val distanceObject = elements.getJSONObject(0).getJSONObject("distance")
                val normalTime= elements.getJSONObject(0).getJSONObject("duration")
                val trafficTime= elements.getJSONObject(0).getJSONObject("duration_in_traffic")
                val distanceVal = distanceObject.getDouble("value")
                val normalTimeVal=normalTime.getDouble("value")
                val trafficTimeVal=trafficTime.getDouble("value")
                normalSpeed=((distanceVal/normalTimeVal)*2.23694);
                trafficSpeed=((distanceVal/trafficTimeVal)*2.23694);
            }
        }
        return (normalSpeed - trafficSpeed).toString()
    }
}
