package com.example.casadesubastas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casadesubastas.database.ItemDatabase;
import com.example.casadesubastas.database.VendorDatabase;
import com.example.casadesubastas.ui.Adaptador;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private ListView lvItem;
    private TextView tvUsuario, tvDinero;
    private ImageButton btnNuevoItem;

    private ArrayList<Item> list;
    private View vItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        tvUsuario = (TextView)findViewById(R.id.usuario);
        tvUsuario.setOnClickListener(this);
        tvDinero = (TextView)findViewById(R.id.dinero);
        tvDinero.setOnClickListener(this);
        btnNuevoItem = (ImageButton)findViewById(R.id.btnNuevoItem);
        btnNuevoItem.setOnClickListener(this);
        lvItem = (ListView)findViewById(R.id.lvItem);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarLogin();
        actualizarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //return super.onCreateOptionsMenu(menu); //Le pasaría la tarea de crear el menú al padre
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.m_insertar:
                insertarItem();
                return true;
            case R.id.m_nuevo_usuario:
                nuevoUsaurio();
                return true;
            case R.id.m_vendedor:
                mostrarVendedores();
                return true;
            case R.id.m_ajustes:
                toast("Pulsaste Ajustes");
                return true;
            case R.id.m_salir:
                cerrarAplicacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    private void insertarItem(){
        if(tvUsuario.getText().equals("")){
            toast(getText(R.string.no_usuario).toString());
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.insertar_item);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Dialog f = (Dialog) dialog;
                    EditText et_foto_uri = (EditText) f.findViewById(R.id.et_foto_uri);
                    EditText et_nombre = (EditText) f.findViewById(R.id.et_nombre);
                    EditText et_descripcion = (EditText) f.findViewById(R.id.et_descripcion);
                    EditText et_precio = (EditText) f.findViewById(R.id.et_precio);

                    Item item = new Item(
                            et_foto_uri.getText().toString(),
                            et_nombre.getText().toString(),
                            et_descripcion.getText().toString(),
                            tvUsuario.getText().toString(),
                            Double.parseDouble(et_precio.getText().toString()));
                    ItemDatabase itemDB = new ItemDatabase(getBaseContext());
                    if (itemDB.insertar(item) > 0)
                        toast(getText(R.string.insertar_ok).toString());
                    actualizarLista();
                }catch (Exception e){
                    toast(getText(R.string.falta_dato).toString());
                }
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

    private void actualizarLista(){
        ItemDatabase itemDB = new ItemDatabase(getBaseContext());
        list = itemDB.buscar();
        ArrayList<Boolean> puedoEditarItem = new ArrayList<>();
        for (Item i:
             list) {
            if(i.getVendedor().equals(tvUsuario.getText().toString()))
                puedoEditarItem.add(true);
            else
                puedoEditarItem.add(false);
        }
        /*
        for(int i=0; i< 50; i++){
            list.add(new Item(
                "",
                "Hago Scroll",
               "Hola",
                    "Yo solo",
                    123
            ));
        }
         */
        Adaptador adaptador = new Adaptador(this, list, puedoEditarItem);
        lvItem.setAdapter(adaptador);

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //Comprar item
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //builder.setView(R.layout.comprar_item);
                builder.setTitle(R.string.info_item);
                builder.setMessage(String.format("%s\n\n%s\n\n%s$",
                        list.get(position).getNombre(), list.get(position).getDescripcion(), list.get(position).getPrecio()));
                builder.setCancelable(false);

                //El usuario conectado no puede comprar su propio producto
                if(!tvUsuario.getText().toString().equals("") && !list.get(position).getVendedor().equals(tvUsuario.getText().toString())){
                    builder.setPositiveButton(R.string.comprar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Le sumamos el dinero al usuario que lo vendió
                            VendorDatabase vendorDB = new VendorDatabase(getBaseContext());
                            vendorDB.sumarDinero(list.get(position).getVendedor(), list.get(position).getPrecio());

                            //Borramos el producto comprado de la lista
                            ItemDatabase itemDB = new ItemDatabase(getBaseContext());
                            if(itemDB.borrar(list.get(position).getId()) > 0){
                                toast(getText(R.string.comprar_ok).toString());
                            }

                            actualizarLista();
                        }
                    });
                }

                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void nuevoUsaurio(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View content =  inflater.inflate(R.layout.cambiar_usuario, null);
        builder.setView(content);

        TextView textView = (TextView) content.findViewById(R.id.tv_usuario);
        textView.setText(R.string.submenu_nuevo_usuario);

        builder.setCancelable(false);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog f = (Dialog)dialog;
                EditText et_usuario = (EditText)f.findViewById(R.id.et_usuario);
                EditText et_password = (EditText)f.findViewById(R.id.et_password);

                VendorDatabase db = new VendorDatabase(getBaseContext());
                Vendor vendor = db.buscarUsuario(et_usuario.getText().toString(), et_password.getText().toString());
                if(vendor != null){
                    toast(getText(R.string.signin_repetido).toString());
                }
                else{
                    Vendor nuevoVendor = new Vendor(
                            "",
                            et_usuario.getText().toString(),
                            et_password.getText().toString(),
                            0
                    );
                    if(db.insertar(nuevoVendor) > 0){
                        toast(getText(R.string.signin_ok).toString());
                        vendor = db.buscarUsuario(et_usuario.getText().toString(), et_password.getText().toString());
                        guardarLogin(vendor.getId()); //Para guardar el login
                        tvUsuario.setText(nuevoVendor.getNombre());
                        tvDinero.setText(String.valueOf(nuevoVendor.getDinero()));
                        actualizarLista(); //Para cambiar los productos que puede editar el nuevo usuario
                    }
                }
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

    private void mostrarVendedores(){
        Intent intent = new Intent(this, VendorActivity.class);
        startActivity(intent);
    }

    private void cerrarAplicacion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.salir)
                .setCancelable(false)
                .setMessage(R.string.salir_mensaje)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.usuario:
                if(tvUsuario.getText().toString().equals(""))
                    login();
                else
                    logout();
                break;
            case R.id.btnNuevoItem:
                insertarItem();
                break;
        }
    }

    private void login(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.cambiar_usuario);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog f = (Dialog)dialog;
                EditText et_usuario = (EditText)f.findViewById(R.id.et_usuario);
                EditText et_password = (EditText)f.findViewById(R.id.et_password);

                VendorDatabase db = new VendorDatabase(getBaseContext());
                Vendor vendor = db.buscarUsuario(et_usuario.getText().toString(), et_password.getText().toString());
                if(vendor != null){
                    tvUsuario.setText(vendor.getNombre());
                    tvDinero.setText(String.valueOf(vendor.getDinero()));
                    guardarLogin(vendor.getId()); //Para mantener sesión iniciada
                    actualizarLista(); //Para cambiar los productos que puede editar el nuevo usuario
                }
                else{
                    toast(getText(R.string.login_fallo).toString());
                }
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

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cerrar_sesion)
                .setCancelable(false)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvUsuario.setText("");
                        tvDinero.setText("");
                        actualizarLista();
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

    public void editarItem(View v){
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        ListView lvParent = (ListView)vwParentRow.getParent();
        Item mItem = list.get(lvParent.indexOfChild(vwParentRow));

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View content =  inflater.inflate(R.layout.insertar_item, null);
        builder.setView(content);
        TextView textView = (TextView) content.findViewById(R.id.tvInsertar_item);
        EditText et_foto_uri = (EditText) content.findViewById(R.id.et_foto_uri);
        EditText et_nombre = (EditText) content.findViewById(R.id.et_nombre);
        EditText et_descripcion = (EditText) content.findViewById(R.id.et_descripcion);
        EditText et_precio = (EditText) content.findViewById(R.id.et_precio);
        TextView tv_modificar_pos = (TextView)content.findViewById(R.id.tv_modificar_pos);

        textView.setText(R.string.modificar_item);
        et_foto_uri.setText(mItem.getFoto_uri());
        et_nombre.setText(mItem.getNombre());
        et_descripcion.setText(mItem.getDescripcion());
        et_precio.setText(String.valueOf(mItem.getPrecio()));
        tv_modificar_pos.setText(String.valueOf(lvParent.indexOfChild(vwParentRow))); //Para saber qué Item modificar

        builder.setCancelable(false);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Dialog f = (Dialog) dialog;
                    EditText et_foto_uri = (EditText) f.findViewById(R.id.et_foto_uri);
                    EditText et_nombre = (EditText) f.findViewById(R.id.et_nombre);
                    EditText et_descripcion = (EditText) f.findViewById(R.id.et_descripcion);
                    EditText et_precio = (EditText) f.findViewById(R.id.et_precio);
                    TextView tv_modificar_pos = (TextView)f.findViewById(R.id.tv_modificar_pos);

                    //Obtener ID
                    int id = list.get(Integer.parseInt(tv_modificar_pos.getText().toString())).getId();

                    Item item = new Item(
                            et_foto_uri.getText().toString(),
                            et_nombre.getText().toString(),
                            et_descripcion.getText().toString(),
                            tvUsuario.getText().toString(),
                            Double.parseDouble(et_precio.getText().toString()));
                    ItemDatabase itemDB = new ItemDatabase(getBaseContext());
                    if (itemDB.modificar(item.toContentValues(), id) > 0)
                        toast(getText(R.string.modificar_ok).toString());
                    actualizarLista();
                }catch (Exception e){
                    toast(getText(R.string.falta_dato).toString());
                }
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

    public void borrarItem(View v){

        vItem = v; //Para usar al aceptar el borrar el producto

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.borrar_producto)
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

        ItemDatabase itemDB = new ItemDatabase(getBaseContext());
        if(itemDB.borrar(list.get(position).getId()) > 0){
            toast(getText(R.string.producto_borrar_ok).toString());
            list.remove(position);
        }
        actualizarLista();
    }

    private void guardarLogin(int id){
        SharedPreferences pf = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pf.edit();
        e.putString("id", String.valueOf(id));
        e.commit();
    }

    private void recuperarLogin(){
        SharedPreferences pf = getSharedPreferences("login", Context.MODE_PRIVATE);
        VendorDatabase db = new VendorDatabase(getBaseContext());
        ArrayList<Vendor> vendorList = db.buscar();
        for (Vendor v:
             vendorList) {
            if(String.valueOf(v.getId()).equals(pf.getString("id", ""))){
                tvUsuario.setText(v.getNombre());
                tvDinero.setText(String.valueOf(v.getDinero()));
            }
        }
    }

    private void toast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

}