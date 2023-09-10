package edu.asu.svellin3.realtimehealthmonitor.transformer

import androidx.room.PrimaryKey

data class MedicalDataLogDTO(
    val id: Int= 0,
    val respiratoryValue: Float,
    val heartRateValue: Float,
    val nauseaValue: Int = 0,
    val headacheValue: Int= 0,
    val diarrheaValue: Int= 0,
    val soarThroatValue: Int= 0,
    val feverValue: Int= 0,
    val muscleAcheValue: Int= 0,
    val lossOfSmellOrTasteValue: Int= 0,
    val cough: Int= 0,
    val shortnessOfBreath: Int= 0,
    val feelingTired: Int= 0
)