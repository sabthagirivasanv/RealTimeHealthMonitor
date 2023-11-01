package edu.asu.svellin3.realtimehealthmonitor.service

import android.os.AsyncTask
import android.util.Log
import edu.asu.svellin3.realtimehealthmonitor.DistanceFinderActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleMapsService {
    companion object{
        suspend fun getDistanceMatrixAsync(origin: DoubleArray, destination: DoubleArray): String? {
            val apiKey = "AIzaSyAqBMOojNv-MPFjRxjjTQZptScz1eDdrXk"

            return try {
                val apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?" +
                        "origins=${origin[0]},${origin[1]}&" +
                        "destinations=${destination[0]},${destination[1]}&" +
                        "departure_time=now&" +
                        "key=$apiKey"

                val result = withContext(Dispatchers.IO) {
                    val url = URL(apiUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = connection.inputStream
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        reader.close()
                        inputStream.close()
                        connection.disconnect()

                        response.toString()
                    } else {
                        null
                    }
                }

                result
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}