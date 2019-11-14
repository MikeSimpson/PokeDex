package nz.co.mikesimpson.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nz.co.mikesimpson.pokedex.ui.pokemon.PokemonMasterFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
