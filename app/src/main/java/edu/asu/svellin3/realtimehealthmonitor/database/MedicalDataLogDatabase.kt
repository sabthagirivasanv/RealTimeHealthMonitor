package edu.asu.svellin3.realtimehealthmonitor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.asu.svellin3.realtimehealthmonitor.dao.MedicalDataLogDao
import edu.asu.svellin3.realtimehealthmonitor.entity.MedicalDataLogEntity

@Database(entities = [MedicalDataLogEntity::class], version = 1, exportSchema = false)
abstract class MedicalDataLogDatabase: RoomDatabase() {
    abstract fun medicalDataLogDao(): MedicalDataLogDao

    companion object{
        @Volatile
        private var INSTANCE: MedicalDataLogDatabase? = null

        fun getDatabase(context: Context): MedicalDataLogDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, MedicalDataLogDatabase::class.java, "medical_data_log_database").build()
                INSTANCE = instance
                return instance
            }
        }


    }
}