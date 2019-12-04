package com.example.casadesubastas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casadesubastas.database.ItemDatabase;
import com.example.casadesubastas.database.VendorDatabase;
import com.example.casadesubastas.ui.VendorAdaptador;

import java.util.ArrayList;

public class VendorActivity extends AppCompatActivity {

    private ArrayList<Vendor> list;
    private ListView lvVendedores;
    private LinearLayout margenArriba, margenAbajo;
    private View vItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        asociar();
        actualizarLista();
    }

    private void asociar(){
        lvVendedores = (ListView)findViewById(R.id.lvVendedores);
        margenArriba = (LinearLayout)findViewById(R.id.llLeyenda);
        margenAbajo = (LinearLayout)findViewById(R.id.margenAbajo);
    }

    private void actualizarLista(){
        VendorDatabase vendorDB = new VendorDatabase(getBaseContext());
        list = vendorDB.buscar();
        VendorAdaptador adaptador = new VendorAdaptador(this, list);
        lvVendedores.setAdapter(adaptador);
        if(list.size() == 0) //No hay objetos
        {
            margenArriba.setVisibility(View.INVISIBLE);
            margenAbajo.setVisibility(View.INVISIBLE);
        }
        else{
            margenArriba.setVisibility(View.VISIBLE);
            margenAbajo.setVisibility(View.VISIBLE);
        }
    }

    public void borrarVendor(View v){

        vItem = v; //Para usar al aceptar el borrar el vendedor

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.borrar_vendedor)
                .setCancelable(false)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aceptarBorrar();
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

    private void aceptarBorrar(){
        LinearLayout vwParentRow = (LinearLayout)vItem.getParent();
        ListView lvParent = (ListView)vwParentRow.getParent();
        int position = lvParent.indexOfChild(vwParentRow);

        VendorDatabase vendorDB = new VendorDatabase(getBaseContext());
        if(vendorDB.borrar(list.get(position).getId()) > 0){
            toast(getText(R.string.vendedor_borrar_ok).toString());
        }

        //Ahora hay que borrar los productos que ha registrado este vendedor
        String nombre = list.get(position).getNombre();
        ItemDatabase itemDB = new ItemDatabase(getBaseContext());
        if(itemDB.borrarVendedor(nombre) > 0){
            toast(getText(R.string.producto_borrar_ok).toString());
        }

        actualizarLista();
    }

    private void toast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
