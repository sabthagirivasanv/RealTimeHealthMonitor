package edu.asu.svellin3.realtimehealthmonitor

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import edu.asu.svellin3.realtimehealthmonitor.entity.MedicalDataLogEntity
import edu.asu.svellin3.realtimehealthmonitor.enums.Symptoms
import edu.asu.svellin3.realtimehealthmonitor.sharedPreference.SymptomsDataSharedPreference
import edu.asu.svellin3.realtimehealthmonitor.transformer.MedicalDataLogDTO
import edu.asu.svellin3.realtimehealthmonitor.transformer.SymptomsData
import edu.asu.svellin3.realtimehealthmonitor.viewModel.MedicalDataLogViewModel

class SymptomActivity : ComponentActivity() {
    private lateinit var symptomsSpinner: Spinner
    private lateinit var rbStar: RatingBar
    private lateinit var medicalDataLogViewModel: MedicalDataLogViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom)
        medicalDataLogViewModel = ViewModelProvider(this).get(MedicalDataLogViewModel::class.java)

        symptomsSpinner = findViewById<Spinner>(R.id.spnSymptoms)
        rbStar = findViewById<RatingBar>(R.id.rbStar)

        val symptomsData = SymptomsDataSharedPreference.loadSymptoms(this)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, symptomsData.map { it.symptomName })
        symptomsSpinner.adapter = adapter

        onSymptomsSpinnerItemSelected(symptomsData)
        onStarRatingChange(symptomsData)

        onUploadData()

    }

    private fun onUploadData() {
        var btnUploadData = findViewById<Button>(R.id.btnUploadData)
        btnUploadData.setOnClickListener {
            var medicalDataLogDTO = SymptomsDataSharedPreference.getRespiratoryAndHeartRateValue(this)
            val symptomsData = SymptomsDataSharedPreference.loadSymptoms(this)
            val newEntry = constructMedicalDataLogEntity(medicalDataLogDTO, symptomsData)
            medicalDataLogViewModel.insertMedicalDataLog(newEntry)
            SymptomsDataSharedPreference.clearSharedPreference(this)
            Toast.makeText(this, "successfully uploaded", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun constructMedicalDataLogEntity(
        medicalDataLogDTO: MedicalDataLogDTO,
        symptomsData: MutableList<SymptomsData>
    ): MedicalDataLogEntity {

        return MedicalDataLogEntity(0,
            medicalDataLogDTO.respiratoryValue,
            medicalDataLogDTO.heartRateValue,
            symptomsData.find { it.symptomName.equals(Symptoms.NAUSEA.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.HEADACHE.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.DIARRHEA.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.SOAR_THROAT.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.FEVER.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.MUSCLE_ACHE.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.LOSS_OF_SMELL_OR_TASTE.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.COUGH.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.SHORTNESS_OF_BREATH.text) }!!.rating,
            symptomsData.find { it.symptomName.equals(Symptoms.FEELING_TIRED.text) }!!.rating
        )
    }

    private fun onSymptomsSpinnerItemSelected(symptomsData: MutableList<SymptomsData>) {
        symptomsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedSymptom = symptomsData[position]
                rbStar.rating = selectedSymptom.rating.toFloat()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

    private fun onStarRatingChange(symptomsData: MutableList<SymptomsData>){
        rbStar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            val selectedSymptom = symptomsData[symptomsSpinner.selectedItemPosition]
            selectedSymptom.rating = rating.toInt()
            SymptomsDataSharedPreference.saveSymptomsRating(this, selectedSymptom)
        }
    }
}