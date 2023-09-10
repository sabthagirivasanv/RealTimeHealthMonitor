package edu.asu.svellin3.realtimehealthmonitor.helper

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class RespiratoryDataProcessor {
    companion object{
        fun processRespiratoryData(
            inputStream: InputStream?,
            columnIndex: Int = 0,
            maxRecords: Int = 1280
        ): Int {
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? =""
            var recordCount = 0
            var ret=0.00

            try {
                var previousValue = 0f
                var currentValue = 0f
                previousValue = 10f
                var k=0
                while (recordCount < maxRecords && reader.readLine().also { line = it } != null) {
                    val columns = line ?.split(",")
                    if (columns != null) {
                        currentValue = sqrt(
                            columns[columnIndex].toDouble().pow(2.0)
                        ).toFloat()
                        if (abs(previousValue - currentValue) > 0.15) {
                            k++
                        }
                        previousValue=currentValue

                    }
                    recordCount++
                }
                ret= (k/45.00)
            }
            catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    reader.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return (30 * ret).toInt()
        }
    }
}