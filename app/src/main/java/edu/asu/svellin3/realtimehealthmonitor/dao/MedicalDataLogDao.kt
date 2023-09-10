package edu.asu.svellin3.realtimehealthmonitor.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.asu.svellin3.realtimehealthmonitor.entity.MedicalDataLogEntity

@Dao
interface MedicalDataLogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMedicalDataLog(medicalDataLogEntity: MedicalDataLogEntity)

    @Query(value = "SELECT * FROM medical_data_log ORDER BY ID DESC")
    fun readAllData(): LiveData<List<MedicalDataLogEntity>>
}