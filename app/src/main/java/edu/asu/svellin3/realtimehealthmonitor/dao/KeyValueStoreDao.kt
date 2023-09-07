package edu.asu.svellin3.realtimehealthmonitor.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.asu.svellin3.realtimehealthmonitor.entity.KeyValueStore

@Dao
interface KeyValueStoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addKeyValueStore(keyValueStore: KeyValueStore)

    @Query(value = "SELECT * FROM key_value_store ORDER BY ID DESC")
    fun readAllData(): LiveData<List<KeyValueStore>>
}