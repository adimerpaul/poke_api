package com.ciscofni.namepokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ciscofni.namepokemon.models.Pokemon;
import com.ciscofni.namepokemon.models.PokemonRespuesta;
import com.ciscofni.namepokemon.pokeapi.PokeapiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;

    ListView listView;
    List<String> datos=new ArrayList<>();
    ArrayAdapter<String> adapter;
    private String TAG="MYTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datos.add("PIKACHU");
        datos.add("CHARMILION");

        listView=(ListView) findViewById(R.id.list);
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datos);
        listView.setAdapter(adapter);
        retrofit= new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        obtenerdatos();
    }

    private void obtenerdatos() {
        PokeapiService service=retrofit.create(PokeapiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall=service.obtenerListaPokemon();
        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                if (response.isSuccessful()){
                    PokemonRespuesta pokemonRespuesta=response.body();
                    ArrayList<Pokemon> listaPokemon=pokemonRespuesta.getResults();
                    datos.clear();
                    for (int i=0;i<listaPokemon.size();i++){
                        Pokemon p=listaPokemon.get(i);
                        Log.e(TAG,"Pokemon: "+p.getName());
                        datos.add(p.getName());
                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                Log.e(TAG,"onFailure "+t.getMessage());
            }
        });
    }
}