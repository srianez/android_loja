package br.com.silas.breja;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

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
       // EditText editText = (EditText) findViewById(R.id.txtValor);
        //editText.addTextChangedListener(new NumberTextWatcher(editText, "##,##"));
    }

    public void salvarItem(View v) {
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
                        Toast.makeText(CadItem.this,
                                "Breja cadastrada com sucesso! :)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CadItem.this,
                                "Ohhh shittt. Erro ao cadastrar breja :(", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public class NumberTextWatcher implements TextWatcher {

        private final DecimalFormat df;
        private final DecimalFormat dfnd;
        private final EditText et;
        private boolean hasFractionalPart;
        private int trailingZeroCount;

        public NumberTextWatcher(EditText editText, String pattern) {
            df = new DecimalFormat(pattern);
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("##,##.00");
            this.et = editText;
            hasFractionalPart = false;
        }

        @Override
        public void afterTextChanged(Editable s) {
            et.removeTextChangedListener(this);

            if (s != null && !s.toString().isEmpty()) {
                try {
                    int inilen, endlen;
                    inilen = et.getText().length();
                    String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("R$","");
                    Number n = df.parse(v);
                    int cp = et.getSelectionStart();
                    if (hasFractionalPart) {
                        StringBuilder trailingZeros = new StringBuilder();
                        while (trailingZeroCount-- > 0)
                            trailingZeros.append('0');
                        et.setText(df.format(n) + trailingZeros.toString());
                    } else {
                        et.setText(dfnd.format(n));
                    }
                    et.setText("$".concat(et.getText().toString()));
                    endlen = et.getText().length();
                    int sel = (cp + (endlen - inilen));
                    if (sel > 0 && sel < et.getText().length()) {
                        et.setSelection(sel);
                    } else if (trailingZeroCount > -1) {
                        et.setSelection(et.getText().length() - 3);
                    } else {
                        et.setSelection(et.getText().length());
                    }
                } catch (NumberFormatException | ParseException e) {
                    e.printStackTrace();
                }
            }

            et.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
            trailingZeroCount = 0;
            if (index > -1) {
                for (index++; index < s.length(); index++) {
                    if (s.charAt(index) == '0')
                        trailingZeroCount++;
                    else {
                        trailingZeroCount = 0;
                    }
                }
                hasFractionalPart = true;
            } else {
                hasFractionalPart = false;
            }
        }
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
