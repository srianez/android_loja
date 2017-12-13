package br.com.silas.breja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    public void goCadItem(View v) {
        Intent proximaTela = new Intent(this, CadItem.class);
        startActivity(proximaTela);
    }
}
