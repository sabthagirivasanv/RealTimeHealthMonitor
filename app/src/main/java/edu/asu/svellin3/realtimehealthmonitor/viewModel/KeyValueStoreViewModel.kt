package edu.asu.svellin3.realtimehealthmonitor.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.asu.svellin3.realtimehealthmonitor.database.KeyValueStoreDatabase
import edu.asu.svellin3.realtimehealthmonitor.entity.KeyValueStore
import edu.asu.svellin3.realtimehealthmonitor.repository.KeyValueStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeyValueStoreViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<KeyValueStore>>

    private val repository: KeyValueStoreRepository

    init {
        val keyValueStoreDao = KeyValueStoreDatabase.getDatabase(application).keyValueStoreDao()
        repository = KeyValueStoreRepository(keyValueStoreDao)
        readAllData = repository.readAllData
    }

    fun addKeyValueStore(keyValueStore: KeyValueStore){
        viewModelScope.launch(Dispatchers.IO){
            repository.addKeyValueStore(keyValueStore)
        }
    }

}