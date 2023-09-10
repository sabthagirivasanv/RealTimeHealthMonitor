package edu.asu.svellin3.realtimehealthmonitor.repository

import androidx.lifecycle.LiveData
import edu.asu.svellin3.realtimehealthmonitor.dao.MedicalDataLogDao
import edu.asu.svellin3.realtimehealthmonitor.entity.MedicalDataLogEntity

class MedicalDataLogRepository(private val medicalDataLogDao: MedicalDataLogDao) {
    val readAllData: LiveData<List<MedicalDataLogEntity>> = medicalDataLogDao.readAllData()

    suspend fun insertMedicalDataLog(medicalDataLogEntity: MedicalDataLogEntity){
        medicalDataLogDao.insertMedicalDataLog(medicalDataLogEntity)
    }
}