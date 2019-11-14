package nz.co.mikesimpson.pokedex.ui.pokemon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nz.co.mikesimpson.pokedex.model.PaginatedResult
import nz.co.mikesimpson.pokedex.model.Pokemon
import nz.co.mikesimpson.pokedex.repository.PokemonService
import retrofit2.Retrofit

class PokemonViewModel : ViewModel() {

    val pokemonList = MutableLiveData<PaginatedResult>()
    val pokemon = MutableLiveData<Pokemon>()
    val errorMessage = MutableLiveData<String>()

    // todo inject these
    val pokemonApi = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .build()
    val pokemonService = pokemonApi.create(PokemonService::class.java)

    init {
        fetchPokemonList()
    }

    private fun fetchPokemonList() = viewModelScope.launch(Dispatchers.IO) {
        val response = pokemonService.listPokemon(limit = 151)
        when (response.code()) {
            in 200..299 -> pokemonList.value = response.body()
            in 400..499 -> errorMessage.value =
                "Oops! We couldn't find any Pokémon, that's probably my fault. Sorry!"
            in 500..599 -> errorMessage.value =
                "Oops! We couldn't find any Pokémon, the API might be down :("
        }
    }

    private fun fetchSinglePokemon(name: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = pokemonService.getSinglePokemon(name)
        when (response.code()) {
            in 200..299 -> pokemon.value = response.body()
            in 400..499 -> errorMessage.value =
                "Oops! We couldn't get details for $name, that's probably our fault. Sorry!"
            in 500..599 -> errorMessage.value =
                "Oops! We couldn't get details for $name, the API might be down :("
        }
    }
}
