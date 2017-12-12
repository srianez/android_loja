package br.com.silas.breja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import br.com.silas.breja.model.Usuario;
import okhttp3.OkHttpClient;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goCadUsuario(View v) {
        Intent proximaTela = new Intent(this, CadUsuario.class);
        //proximaTela.putExtra("TIMEVISITANTE", etTimeVisitante.getText().toString());
        //proximaTela.putExtra("TIMECASA", etTimeCasa.getText().toString());
        startActivity(proximaTela);
    }

    public void goMenu(View v) {

/*        BrejaAPI api = getRetrofit().create(BrejaAPI.class);

        api.buscarUsuario(etUsuario.getText().toString())
                .enqueue(new Callback<Usuario>() {

                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                        if (response.body().getUsuario() == null) {
                            Toast.makeText(MainActivity.this,
                                    "Usuário não encontrado!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(MainActivity.this,
                                "Deu erro", Toast.LENGTH_LONG).show();
                    }
                });*/

                Intent proximaTela = new Intent(this, MenuPrincipal.class);
                startActivity(proximaTela);
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
