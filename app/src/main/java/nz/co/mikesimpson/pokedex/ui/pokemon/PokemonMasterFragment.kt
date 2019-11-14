package nz.co.mikesimpson.pokedex.ui.pokemon

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nz.co.mikesimpson.pokedex.R

class PokemonMasterFragment : Fragment() {

    companion object {
        fun newInstance() = PokemonMasterFragment()
    }

    private lateinit var viewModel: PokemonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pokemon_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PokemonViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
