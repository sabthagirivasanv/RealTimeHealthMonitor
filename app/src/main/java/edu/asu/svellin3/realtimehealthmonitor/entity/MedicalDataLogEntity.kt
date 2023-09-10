package edu.asu.svellin3.realtimehealthmonitor.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medical_data_log")
data class MedicalDataLogEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val respiratoryValue: Float,
    val heartRateValue: Float,
    val nauseaValue: Int,
    val headacheValue: Int,
    val diarrheaValue: Int,
    val soarThroatValue: Int,
    val feverValue: Int,
    val muscleAcheValue: Int,
    val lossOfSmellOrTasteValue: Int,
    val cough: Int,
    val shortnessOfBreath: Int,
    val feelingTired: Int
)