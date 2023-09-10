package edu.asu.svellin3.realtimehealthmonitor.sharedPreference

import android.content.Context
import edu.asu.svellin3.realtimehealthmonitor.enums.Symptoms
import edu.asu.svellin3.realtimehealthmonitor.transformer.MedicalDataLogDTO
import edu.asu.svellin3.realtimehealthmonitor.transformer.SymptomsData

class SymptomsDataSharedPreference {
    companion object {

        private const val SHARED_PREFERENCE_NAME = "SymptomsData"
        fun loadSymptoms(context: Context): MutableList<SymptomsData> {
            val loadedSymptoms = mutableListOf<SymptomsData>()
            var sharedPreferences =
                context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            for (eachSymptom in Symptoms.values()) {
                val savedData = sharedPreferences.getFloat(eachSymptom.text, 0f)
                loadedSymptoms.add(SymptomsData(eachSymptom.text, savedData.toInt()))
            }
            return loadedSymptoms
        }

        fun saveSymptomsRating(context: Context, symptomsData: SymptomsData) {
            var sharedPreferences =
                context.getSharedPreferences("SymptomsData", Context.MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putFloat(symptomsData.symptomName, symptomsData.rating.toFloat())
            editor.apply()
        }

        fun saveData(context: Context, key: String, value: Float) {
            var sharedPreferences =
                context.getSharedPreferences("SymptomsData", Context.MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        fun getRespiratoryAndHeartRateValue(context: Context): MedicalDataLogDTO {

            var sharedPreferences =
                context.getSharedPreferences("SymptomsData", Context.MODE_PRIVATE)

            var respiratoryValue = sharedPreferences.getFloat("respiratoryValue", 0f)
            var heartRateValue = sharedPreferences.getFloat("heartRateValue", 0f)

            return MedicalDataLogDTO(respiratoryValue = respiratoryValue,heartRateValue = heartRateValue)
        }

        fun clearSharedPreference(context: Context ){
            var sharedPreferences =
                context.getSharedPreferences("SymptomsData", Context.MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }
}