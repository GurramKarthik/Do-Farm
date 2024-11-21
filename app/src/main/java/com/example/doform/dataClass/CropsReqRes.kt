package com.example.doform.dataClass

data class Crop(
    val name: String,
    val description: String,
    val approximateNumberOfDays: Int,
    val pesticides: Pesticides,
    val irrigationType: String
)

data class Pesticides(
    val organic: List<String>,
    val chemical: List<String>
)





