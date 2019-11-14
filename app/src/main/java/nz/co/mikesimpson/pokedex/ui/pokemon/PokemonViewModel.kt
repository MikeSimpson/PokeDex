package nz.co.mikesimpson.pokedex.ui.pokemon

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.co.mikesimpson.pokedex.model.PaginatedResult
import nz.co.mikesimpson.pokedex.model.Pokemon
import nz.co.mikesimpson.pokedex.repository.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PokemonViewModel : ViewModel() {

    private val _pokemonList = MutableLiveData<PaginatedResult>()
    val pokemonList: LiveData<PaginatedResult> = _pokemonList

    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> = _pokemon

    private val _listLoading = MutableLiveData<Boolean>().apply { value = false }
    val listLoading: LiveData<Boolean> = _listLoading

    private val _pokemonLoading = MutableLiveData<Boolean>().apply { value = false }
    val pokemonLoading: LiveData<Boolean> = _pokemonLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // todo inject these
    private val pokemonApi = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
    private val pokemonService = pokemonApi.create(PokemonService::class.java)

    init {
        fetchPokemonList()
    }

    fun fetchPokemonList() = viewModelScope.launch {
        _listLoading.value = true

        val response = withContext((Dispatchers.IO)) {
            pokemonService.listPokemon(limit = 151)
        }
        when (response.code()) {
            in 200..299 -> _pokemonList.value = response.body()
            in 500..599 -> {
                _errorMessage.value =
                    "Oops! We couldn't find any Pokémon, the API might be down :("
                Log.e("Pokemon", response.message())
            }
            else -> {
                _errorMessage.value =
                    "Oops! We couldn't find any Pokémon, that's probably my fault. Sorry!"
                Log.e("Pokemon", response.message())
            }
        }
        _listLoading.value = false
    }

    fun fetchSinglePokemon(name: String) = viewModelScope.launch {
        if (_pokemon.value?.name != name) _pokemon.value = null

        _pokemonLoading.value = true
        val response = withContext((Dispatchers.IO)) {
            pokemonService.getSinglePokemon(name)
        }
        when (response.code()) {
            in 200..299 -> _pokemon.value = response.body()
            in 500..599 -> {
                _errorMessage.value =
                    "Oops! We couldn't get details for $name, the API might be down :("
                Log.e("Pokemon", response.message())
            }
            else -> {
                _errorMessage.value =
                    "Oops! We couldn't get details for $name, that's probably our fault. Sorry!"
                Log.e("Pokemon", response.message())
            }
        }
        _pokemonLoading.value = false
    }
}
