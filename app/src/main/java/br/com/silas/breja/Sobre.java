package br.com.silas.breja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Sobre extends AppCompatActivity {

    private Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sobre, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // verifica qual foi selecionado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Valida o item de menu_minhas_brejas escolhido
        if (item.getItemId() == R.id.menu_nova_breja){
            intent = new Intent(this,CadBreja.class);
            Sobre.this.finish();
            startActivity(intent);

        } else if(item.getItemId() == R.id.menu_minhas_brejas) {
            intent = new Intent(this, ListarBrejas.class);
            Sobre.this.finish();
            startActivity(intent);

        } else if(item.getItemId() == R.id.menu_sair) {
            intent = new Intent(this, LoginActivity.class);
            Sobre.this.finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
    }
}
