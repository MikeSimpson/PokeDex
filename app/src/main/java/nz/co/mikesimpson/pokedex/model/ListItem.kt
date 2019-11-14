package nz.co.mikesimpson.pokedex.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListItem(
    val name: String,
    val url: String
)