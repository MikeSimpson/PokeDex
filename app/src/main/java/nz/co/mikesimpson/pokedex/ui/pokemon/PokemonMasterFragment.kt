package nz.co.mikesimpson.pokedex.ui.pokemon

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                findNavController().navigate(PokemonMasterFragmentDirections.goToPokemonDetail(it.name.capitalize()))
            }
            binding.recyclerView.adapter = adapter
            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (recyclerView.hasScrolledToBottom()) {
                        viewModel.fetchPokemonList()
                    }
                }
            })
            View.GONE

            binding.swipeRefresh.setOnRefreshListener {
                viewModel.fetchPokemonList(0)
            }

            subscribePokemonList()
        }
    }

    private fun RecyclerView.hasScrolledToBottom(): Boolean {
        val layoutManager = layoutManager as? LinearLayoutManager
        val itemCount = viewModel.filteredList.value?.size
        val lastItem = layoutManager?.findLastCompletelyVisibleItemPosition()
        return lastItem == (itemCount?.minus(1)) || lastItem == RecyclerView.NO_POSITION
    }

    private fun subscribePokemonList() {
        viewModel.filteredList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                if (binding.recyclerView.hasScrolledToBottom()) {
                    viewModel.fetchPokemonList()
                }
            }
        })
        viewModel.listLoading.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it == true
        })
    }
}
