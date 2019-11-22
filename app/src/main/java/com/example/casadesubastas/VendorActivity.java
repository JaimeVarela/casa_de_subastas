package com.example.casadesubastas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VendorActivity extends AppCompatActivity {

    private ListView lvVendedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        asociar();
        iniciar();
    }

    private void asociar(){
        lvVendedores = (ListView)findViewById(R.id.lvVendedores);
    }

    private void iniciar(){

        if(getIntent().getStringArrayExtra("nombre") != null
        && getIntent().getStringArrayExtra("dinero") != null){
            String[] nombre = getIntent().getStringArrayExtra("nombre");
            String[] dinero = getIntent().getStringArrayExtra("dinero");

            String[] vendedores = new String[nombre.length];
            for(int i = 0; i < vendedores.length; i++){
                vendedores[i] = String.format("%s\t%s$", nombre[i], dinero[i]);
            }

            ArrayAdapter<String> adaptador =
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vendedores);
            lvVendedores.setAdapter(adaptador);
        }
    }
}
