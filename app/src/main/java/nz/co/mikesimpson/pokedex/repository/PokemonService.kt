package nz.co.mikesimpson.pokedex.repository

import nz.co.mikesimpson.pokedex.model.PaginatedResult
import nz.co.mikesimpson.pokedex.model.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun listPokemon(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): Response<PaginatedResult>

    @GET("pokemon/{name}")
    suspend fun getSinglePokemon(
        @Path("name") name: String
    ): Response<Pokemon>
}