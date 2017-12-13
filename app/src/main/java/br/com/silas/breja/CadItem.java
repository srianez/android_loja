package br.com.silas.breja;

import android.app.ProgressDialog;
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

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_item);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtTipo = (EditText) findViewById(R.id.txtTipo);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);

        txtValor = (EditText) findViewById(R.id.txtValor);

        progressDialog = new ProgressDialog(this);
    }

    public void salvarItem(View v) {
        showProgress("Breja", "Salvando a breja...");

        BrejaAPI api = getRetrofit().create(BrejaAPI.class);

        if(txtNome.getText().toString().isEmpty()) {
            Toast.makeText(CadItem.this, "Informe ao menos o nome da breja né fera?!", Toast.LENGTH_LONG).show();
        }

        Item item = new Item();

        item.setNome(txtNome.getText().toString());
        item.setTipo(txtTipo.getText().toString());
        item.setDescricao(txtDescricao.getText().toString());
        item.setValor(txtValor.getText().toString());

        api.salvarItem(item)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        dismissProgress();
                        Toast.makeText(CadItem.this,
                                "Breja cadastrada com sucesso! :)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(CadItem.this,
                                "Ohhh shittt. Erro ao cadastrar breja :(", Toast.LENGTH_SHORT).show();

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

    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(CadItem.this);

        if(!progressDialog.isShowing()) {
            progressDialog.setMessage(mensagem);
            progressDialog.setTitle(titulo);
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }

}
