package nz.co.mikesimpson.pokedex.ui.pokemon

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.*
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.co.mikesimpson.pokedex.model.ListItem
import nz.co.mikesimpson.pokedex.model.PaginatedResult
import nz.co.mikesimpson.pokedex.model.Pokemon
import nz.co.mikesimpson.pokedex.repository.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PokemonViewModel : ViewModel() {

    private val paginatedList = MutableLiveData<PaginatedResult>()
    val pokemonFilter = MutableLiveData<String>().apply { value = "" }

    private val _filteredList = MediatorLiveData<List<ListItem>>().apply {
        addSource(pokemonFilter) {
            this.value = getFilteredList(paginatedList.value, it)
        }
        addSource(paginatedList) {
            this.value = getFilteredList(it, pokemonFilter.value)
        }
    }
    val filteredList: LiveData<List<ListItem>> = _filteredList

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

    fun fetchPokemonList(offset: Int = paginatedList.value?.results?.size ?: 0) {
        if (paginatedList.value?.count == paginatedList.value?.results?.size && offset != 0) return

        viewModelScope.launch {
            _listLoading.value = true
            _errorMessage.value = null

            try {
                val response = withContext((Dispatchers.IO)) {
                    pokemonService.listPokemon(offset = offset)
                }
                when (response.code()) {
                    in 200..299 -> {
                        response.body()?.let { result ->
                            val combinedList = paginatedList.value?.results?.toMutableList()
                            combinedList?.addAll(result.results)
                            paginatedList.value = combinedList?.let {
                                result.copy(results = it)
                            } ?: result
                        }
                    }
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
            } catch (e: Exception) { // todo don't catch generic exception..
                _errorMessage.value = e.message
            }
            _listLoading.value = false
        }
    }

    fun fetchSinglePokemon(name: String) = viewModelScope.launch {
        if (_pokemon.value?.name != name) _pokemon.value = null

        _pokemonLoading.value = true
        _errorMessage.value = null

        try {
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
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
        _pokemonLoading.value = false
    }

    private fun getFilteredList(list: PaginatedResult?, filter: String?): List<ListItem>? {
        val fullList = paginatedList.value?.results
        return fullList?.filter { item ->
            item.name.startsWith(pokemonFilter.value.toString(), ignoreCase = true)
        }
    }
}
