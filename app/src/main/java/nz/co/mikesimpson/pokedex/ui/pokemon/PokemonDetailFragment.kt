package nz.co.mikesimpson.pokedex.ui.pokemon

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
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

        subscribePokemon()
    }

    private fun subscribePokemon() {
        viewModel.pokemon.observe(viewLifecycleOwner, Observer { pokemon ->
            pokemon?.sprites?.frontDefault?.let { sprite ->
                context?.let { ctx ->
                    Glide.with(ctx)
                        .load(sprite)
                        .into(binding.ivSprite)
                }
            }
        })
    }
}
