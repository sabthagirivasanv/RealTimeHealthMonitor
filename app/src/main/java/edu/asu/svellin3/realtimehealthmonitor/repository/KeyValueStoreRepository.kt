package edu.asu.svellin3.realtimehealthmonitor.repository

import androidx.lifecycle.LiveData
import edu.asu.svellin3.realtimehealthmonitor.dao.KeyValueStoreDao
import edu.asu.svellin3.realtimehealthmonitor.entity.KeyValueStore

class KeyValueStoreRepository(private val keyValueStoreDao: KeyValueStoreDao) {
    val readAllData: LiveData<List<KeyValueStore>> = keyValueStoreDao.readAllData()

    suspend fun addKeyValueStore(keyValueStore: KeyValueStore){
        keyValueStoreDao.addKeyValueStore(keyValueStore)
    }
}