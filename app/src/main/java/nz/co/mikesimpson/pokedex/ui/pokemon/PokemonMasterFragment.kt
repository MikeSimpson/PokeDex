package nz.co.mikesimpson.pokedex.ui.pokemon

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import nz.co.mikesimpson.pokedex.R
import nz.co.mikesimpson.pokedex.databinding.PokemonMasterFragmentBinding
import nz.co.mikesimpson.pokedex.ui.common.BaseFragment
import nz.co.mikesimpson.pokedex.ui.common.ListItemAdapter
import nz.co.mikesimpson.pokedex.ui.common.autoCleared

class PokemonMasterFragment :
    BaseFragment<PokemonMasterFragmentBinding>(R.layout.pokemon_master_fragment) {

    private lateinit var viewModel: PokemonViewModel

    private var adapter by autoCleared<ListItemAdapter>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            viewModel = ViewModelProviders.of(activity).get(PokemonViewModel::class.java)
            binding.viewModel = viewModel

            adapter = ListItemAdapter {
                viewModel.fetchSinglePokemon(it.name)
                findNavController().navigate(PokemonMasterFragmentDirections.goToPokemonDetail())
            }
            binding.recyclerView.adapter = adapter

            binding.swipeRefresh.setOnRefreshListener {
                viewModel.fetchPokemonList()
            }

            subscribePokemonList()
        }
    }

    private fun subscribePokemonList() {
        viewModel.pokemonList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.results)
        })
        viewModel.listLoading.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
    }
}
