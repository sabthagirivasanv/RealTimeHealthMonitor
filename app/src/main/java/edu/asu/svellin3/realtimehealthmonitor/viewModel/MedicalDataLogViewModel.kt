package edu.asu.svellin3.realtimehealthmonitor.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.asu.svellin3.realtimehealthmonitor.database.MedicalDataLogDatabase
import edu.asu.svellin3.realtimehealthmonitor.entity.MedicalDataLogEntity
import edu.asu.svellin3.realtimehealthmonitor.repository.MedicalDataLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicalDataLogViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<MedicalDataLogEntity>>

    private val repository: MedicalDataLogRepository

    init {
        val keyValueStoreDao = MedicalDataLogDatabase.getDatabase(application).medicalDataLogDao()
        repository = MedicalDataLogRepository(keyValueStoreDao)
        readAllData = repository.readAllData
    }

    fun insertMedicalDataLog(medicalDataLogEntity: MedicalDataLogEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertMedicalDataLog(medicalDataLogEntity)
        }
    }

}