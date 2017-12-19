package br.com.silas.breja;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.silas.breja.api.BrejaAPI;
import br.com.silas.breja.model.Item;
import br.com.silas.breja.util.ClickRecyclerView_Interface;
import br.com.silas.breja.util.RecyclerAdapter;
import br.com.silas.breja.util.SessionRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListarBrejas extends AppCompatActivity implements ClickRecyclerView_Interface {

    private SessionRepository sr = new SessionRepository();
    private EditText etFiltroListaBreja;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerAdapter adapter;
    private List<Item> brejasListas = new ArrayList<>();
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_brejas);

        onKeyUpEditText();
        setaRecyclerView("");

    }
    public void setaRecyclerView(String filtro){

        //Aqui é instanciado o Recyclerview
        if(!filtro.equals("") && filtro !=null) {
            retornaBrejasSincronizado(filtro);
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_recyclerbreja);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            adapter = new RecyclerAdapter(this, brejasListas, this);
            mRecyclerView.setAdapter(adapter);
        } else {
            retornaBrejasSincronizado();
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_recyclerbreja);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            adapter = new RecyclerAdapter(this, brejasListas, this);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void onKeyUpEditText()
    {
        etFiltroListaBreja = (EditText) findViewById(R.id.etFiltroListaBreja);
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setaRecyclerView(etFiltroListaBreja.getText().toString());
            }
        };
        etFiltroListaBreja.addTextChangedListener(fieldValidatorTextWatcher);
    }

    @Override
    public void onCustomClick(Object object)
    {
        Item i = (Item) object;

        if(i==null)
        {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Breja não encontrada", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(ListarBrejas.this, ManterBreja.class);

            intent.putExtra("Item", i);
            startActivity(intent);
        }
    }

    @Override
    public void onCloseButton(Object object)
    {
        final Item u = (Item) object;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(getWindow().getDecorView().getRootView().getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getWindow().getDecorView().getRootView().getContext());
        }
        builder.setTitle("Remover breja")
                .setMessage("Deseja remover essa breja?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        apagaBreja(u.getId());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Faça nada
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public Retrofit getRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl("https://silasloja.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void retornaBrejasSincronizado()
    {
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

    private void retornaBrejasSincronizado(final String filtro)
    {
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    BrejaAPI api = getRetrofit().create(BrejaAPI.class);
                    brejasListas = api.buscarItemNomeParc(filtro).execute().body();
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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void apagaBreja(String id)
    {
        BrejaAPI api = getRetrofit().create(BrejaAPI.class);

        api.deleteById(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.msgDeletarBrejaOk), Snackbar.LENGTH_LONG).show();
                setaRecyclerView("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.erroConexao), Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
