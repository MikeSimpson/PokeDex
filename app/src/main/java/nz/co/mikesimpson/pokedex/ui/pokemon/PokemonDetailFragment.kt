package nz.co.mikesimpson.pokedex.ui.pokemon

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nz.co.mikesimpson.pokedex.R
import nz.co.mikesimpson.pokedex.databinding.PokemonDetailFragmentBinding
import nz.co.mikesimpson.pokedex.ui.common.BaseFragment

class PokemonDetailFragment :
    BaseFragment<PokemonDetailFragmentBinding>(R.layout.pokemon_detail_fragment) {
    private lateinit var viewModel: PokemonViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            viewModel = ViewModelProviders.of(activity).get(PokemonViewModel::class.java)
            binding.viewModel = viewModel
        }
    }
}
