package edu.asu.svellin3.realtimehealthmonitor.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_value_store")
data class KeyValueStore (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val key: String,
    val value: String
)