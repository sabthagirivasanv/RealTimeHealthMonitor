package edu.asu.svellin3.realtimehealthmonitor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.asu.svellin3.realtimehealthmonitor.dao.KeyValueStoreDao
import edu.asu.svellin3.realtimehealthmonitor.entity.KeyValueStore

@Database(entities = [KeyValueStore::class], version = 1, exportSchema = false)
abstract class KeyValueStoreDatabase: RoomDatabase() {
    abstract fun keyValueStoreDao(): KeyValueStoreDao

    companion object{
        @Volatile
        private var INSTANCE: KeyValueStoreDatabase? = null

        fun getDatabase(context: Context): KeyValueStoreDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KeyValueStoreDatabase::class.java,
                    "key_value_store_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


    }
}