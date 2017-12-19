package br.com.silas.breja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import br.com.silas.breja.api.BrejaAPI;
import br.com.silas.breja.model.Breja;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManterBreja extends AppCompatActivity {

    private Breja i;

    ListarBrejas listarBrejas = new ListarBrejas();

    private AutoCompleteTextView txtId, txtNome, txtTipo, txtDescricao;
    private FloatingActionButton fab;
    private Button btSalvarDados;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_breja);

        //comentei o código no activity_manter_breja.xml pra colocar o menu nesse activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        i = (Breja) getIntent().getSerializableExtra("Breja");

        txtId = (AutoCompleteTextView) findViewById(R.id.txtId);
        txtNome = (AutoCompleteTextView) findViewById(R.id.txtNome);
        txtTipo = (AutoCompleteTextView) findViewById(R.id.txtTipo);
        txtDescricao = (AutoCompleteTextView) findViewById(R.id.txtDescricao);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btSalvarDados = (Button) findViewById(R.id.btSalvarDados);

        progressDialog = new ProgressDialog(this);

        preencheCampos();

    }

    private void preencheCampos() {
        txtId.setText(i.getId());
        txtNome.setText(i.getNome());
        txtTipo.setText(i.getTipo());
        txtDescricao.setText(i.getDescricao());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void liberaCamposEdicao(View v) {
        txtNome.setEnabled(true);
        txtTipo.setEnabled(true);
        txtDescricao.setEnabled(true);
        txtNome.requestFocus();
        fab.setVisibility(View.GONE);
        btSalvarDados.setVisibility(View.VISIBLE);
    }

    private void bloqueaCampos() {
        txtNome.setEnabled(false);
        txtTipo.setEnabled(false);
        txtDescricao.setEnabled(false);
        fab.setVisibility(View.VISIBLE);
        btSalvarDados.setVisibility(View.GONE);
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://silasloja.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void escondeTeclado(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private boolean verificaCampos() {
        if (txtNome.getText().toString().length() == 0) {
            txtNome.setError("Informar o nome da breja!");
            return false;
        } else if (txtTipo.getText().toString().length() == 0) {
            txtTipo.setError("Informar o tipo da breja!");
            return false;
        } else {
            return true;
        }
    }

    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(ManterBreja.this);
        if(!progressDialog.isShowing()) {
            progressDialog.setMessage(mensagem);
            progressDialog.setTitle(titulo);
            progressDialog.show();
        }
    }

    public void salvarDadosBreja(final View v)
    {
        showProgress("Breja", "Alterando a breja...");

        if(verificaCampos())
        {
                BrejaAPI api = getRetrofit().create(BrejaAPI.class);
                 Breja u = new Breja(txtId.getText().toString(),
                                     txtNome.getText().toString(),
                                     txtTipo.getText().toString(),
                                     txtDescricao.getText().toString());
                api.atualizar(u).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)
                    {
                        Toast.makeText(ManterBreja.this, getString(R.string.msgAltBrejaSucess), Toast.LENGTH_SHORT).show();
                        bloqueaCampos();
                        escondeTeclado(v);
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t)
                    {
                        System.out.println(t);
                        Toast.makeText(ManterBreja.this, getString(R.string.msgAltBrejaFail), Toast.LENGTH_SHORT).show();
                        escondeTeclado(v);
                        dismissProgress();
                    }
                });
        }

    }

    private void dismissProgress() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }

}
