package com.example.casadesubastas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.casadesubastas.database.VendorDatabase;

public class VendorActivity extends AppCompatActivity {

    private String[] nombre, dinero;
    private ListView lvVendedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        asociar();
        iniciar();
        eventos();
    }

    private void asociar(){
        lvVendedores = (ListView)findViewById(R.id.lvVendedores);
    }

    private void iniciar(){

        if(getIntent().getStringArrayExtra("nombre") != null
        && getIntent().getStringArrayExtra("dinero") != null){
            nombre = getIntent().getStringArrayExtra("nombre");
            dinero = getIntent().getStringArrayExtra("dinero");

            String[] vendedores = new String[nombre.length];
            for(int i = 0; i < vendedores.length; i++){
                vendedores[i] = String.format("%s\t%s$", nombre[i], dinero[i]);
            }

            ArrayAdapter<String> adaptador =
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vendedores);
            lvVendedores.setAdapter(adaptador);
        }
    }

    private void eventos(){
        lvVendedores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position);
            }
        });
    }

    private void dialogo(int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format("%s: %s\n%s: %s",
                getText(R.string.nombre).toString(), nombre[position], getText(R.string.dinero).toString(), dinero[position]));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
