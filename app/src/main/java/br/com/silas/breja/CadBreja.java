package br.com.silas.breja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import br.com.silas.breja.api.BrejaAPI;

import br.com.silas.breja.model.Breja;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadBreja extends AppCompatActivity {

    private Intent intent;

    private EditText txtNome;
    private EditText txtTipo;
    private EditText txtDescricao;

    private ProgressDialog progressDialog;

    // cria o menu_minhas_brejas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nova_breja, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // verifica qual foi selecionado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Valida o item de menu_minhas_brejas escolhido
        if (item.getItemId() == R.id.menu_minhas_brejas){
            intent = new Intent(this,ListarBrejas.class);
            startActivity(intent);

        } else if(item.getItemId() == R.id.menu_sair) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if(item.getItemId() == R.id.menu_sobre) {
            intent = new Intent(this, Sobre.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_breja);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtTipo = (EditText) findViewById(R.id.txtTipo);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);

        progressDialog = new ProgressDialog(this);
    }

    public void salvarItem(View v) {

        if(txtNome.getText().toString().isEmpty()) {
            Toast.makeText(CadBreja.this, "Informe ao menos o nome da breja né fera?!", Toast.LENGTH_LONG).show();
        }

        showProgress("Breja", "Salvando a breja...");

        BrejaAPI api = getRetrofit().create(BrejaAPI.class);

        Breja item = new Breja();

        item.setNome(txtNome.getText().toString());
        item.setTipo(txtTipo.getText().toString());
        item.setDescricao(txtDescricao.getText().toString());

        api.salvarItem(item)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        dismissProgress();
                        Toast.makeText(CadBreja.this,
                                "Breja cadastrada com sucesso! :)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(CadBreja.this,
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
            progressDialog = new ProgressDialog(CadBreja.this);

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
