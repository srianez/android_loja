package br.com.silas.breja;

import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.silas.breja.api.BrejaAPI;
import br.com.silas.breja.model.Item;
import br.com.silas.breja.util.ClickRecyclerView_Interface;
import br.com.silas.breja.util.RecyclerTesteAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListarBrejas extends AppCompatActivity implements ClickRecyclerView_Interface {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerTesteAdapter adapter;
    private List<Item> brejasListas = new ArrayList<>();
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_brejas);

        setaRecyclerView();

        setaButtons();
        //listenersButtons();
    }
    public void setaRecyclerView(){

        //Aqui é instanciado o Recyclerview
        retornaBrejasSincronizado();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_recyclerteste);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerTesteAdapter(this, brejasListas, this);
        mRecyclerView.setAdapter(adapter);
    }

    public void setaButtons(){

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_fabteste);

    }
    @Override
    public void onCustomClick(Object object) {
        Item item = (Item) object;
        String nome = item.getNome();

    }


    public void listenersButtons() {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Cria uma nova pessoa chamada Renan Teles
                Item item = new Item();
                item.setNome("Renan Teles");

                //Adiciona a pessoa1 e avisa o adapter que o conteúdo
                //da lista foi alterado
                brejasListas.add(item);
                adapter.notifyDataSetChanged();

            }
        });
    }

    public Retrofit getRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl("https://silasloja.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void retornaBrejasSincronizado()
    { // metodo sincrono precisa estar em uma thread, start para iniciar e join pra esperar terminar
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    BrejaAPI api = getRetrofit().create(BrejaAPI.class);
                    brejasListas = api.findAll().execute().body();
                }
                catch(NetworkOnMainThreadException | IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void apagaUsuario(String id)
    {
        BrejaAPI api = getRetrofit().create(BrejaAPI.class);

        api.deleteById(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.msgDeletarUsuarioOk), Snackbar.LENGTH_LONG).show();
                setaRecyclerView();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.erroConexao), Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
