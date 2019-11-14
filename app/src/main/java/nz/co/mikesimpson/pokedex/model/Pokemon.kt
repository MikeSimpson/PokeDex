package nz.co.mikesimpson.pokedex.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pokemon(
    val name: String,
    val height: Int,
    val weight: Int
)