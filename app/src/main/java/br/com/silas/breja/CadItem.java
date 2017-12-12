package br.com.silas.breja;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import br.com.silas.breja.api.BrejaAPI;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import br.com.silas.breja.model.Item;


public class CadItem extends AppCompatActivity {

    private EditText txtNome;
    private EditText txtTipo;
    private EditText txtDescricao;
    private EditText txtValor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_item);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtTipo = (EditText) findViewById(R.id.txtTipo);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        txtValor = (EditText) findViewById(R.id.txtValor);
    }

    public void salvarUsuario(View v) {
        BrejaAPI api = getRetrofit().create(BrejaAPI.class);

        Item item = new Item();

        item.setNome(txtNome.getText().toString());
        item.setNome(txtTipo.getText().toString());
        item.setNome(txtDescricao.getText().toString());
        item.setNome(txtValor.getText().toString());

        api.salvarItem(item)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(CadItem.this,
                                "Gravado com sucesso!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CadItem.this,
                                "Deu ruim", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private Retrofit getRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://silasloja.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

}
