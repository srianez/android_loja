package br.com.silas.breja;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import br.com.silas.breja.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import br.com.silas.breja.api.BrejaAPI;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etSenha;
    private EditText etEmail;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = (EditText) findViewById(R.id.txUsuario);
        etSenha = (EditText) findViewById(R.id.txtPassword);

        mProgressView = (ProgressBar) findViewById(R.id.login_progressL);

    }

    public void goCadUsuario(View v) {
        Intent proximaTela = new Intent(this, CadUsuario.class);
        startActivity(proximaTela);
    }

    public void goMenu2(View v) {
       Intent proximaTela = new Intent(this, MenuPrincipal.class);
       startActivity(proximaTela);
    }

    public void goMenu(View v) {

        if (verificaCamposObrigatorios()){
                escondeTeclado(v);
                mProgressView.setVisibility(View.VISIBLE);

                BrejaAPI api = getRetrofit().create(BrejaAPI.class);
                api.buscarUsuario(etUsuario.getText().toString(), etSenha.getText().toString())
                        .enqueue(new Callback<Usuario>() {

                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                Intent intent = new Intent(MainActivity.this,
                                        MenuPrincipal.class);
                                MainActivity.this.finish();
                                startActivity(intent);
                                mProgressView.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                                Toast.makeText(MainActivity.this,
                                        "Usuário não encontrado!", Toast.LENGTH_LONG).show();
                                etUsuario.setText("");
                                etSenha.setText("");
                                etUsuario.requestFocus();
                                mProgressView.setVisibility(View.GONE);
                            }
                        });
        }

    }

    private boolean verificaCamposObrigatorios()
    {
        if(etUsuario.getText().toString().length()==0)
        {
            etUsuario.setError("Informar o usuário!!");
            return false;
        }
        else if((etSenha.getText().toString().length()==0))
        {
            etSenha.setError("Informar a senha!");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void escondeTeclado(View v)
    {
        if (v != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private Retrofit getRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl("https://silasloja.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
