package nz.co.mikesimpson.pokedex.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginatedResult (
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ListItem>
)