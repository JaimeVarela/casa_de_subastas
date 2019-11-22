package com.example.casadesubastas.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casadesubastas.Item;
import com.example.casadesubastas.R;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter{

    private Context context;
    private ArrayList<Item> listItems;
    private ArrayList<Boolean> puedoEditarItem;

    public Adaptador(Context context, ArrayList<Item> listItems, ArrayList<Boolean> puedoEditarItem) {
        this.context = context;
        this.listItems = listItems;
        this.puedoEditarItem = puedoEditarItem;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listItems.get(position).getId();
    }

    public void setList(ArrayList<Item> list){
        listItems = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item)getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_main, null);

        ImageView imgFoto = (ImageView)convertView.findViewById(R.id.item_foto);
        TextView nombre = (TextView)convertView.findViewById(R.id.item_nombre);
        TextView vendedor = (TextView)convertView.findViewById(R.id.item_vendedor);
        TextView precio = (TextView)convertView.findViewById(R.id.item_precio);

        imgFoto.setImageResource(R.mipmap.ic_launcher_round);
        nombre.setText(item.getNombre());
        vendedor.setText(item.getVendedor());
        precio.setText(String.valueOf(item.getPrecio()));

        ImageView imgEdit = (ImageView)convertView.findViewById(R.id.item_editar);
        ImageView imgBorrar = (ImageView)convertView.findViewById(R.id.item_borrar);

        if(!puedoEditarItem.get(position)){
            imgEdit.setVisibility(View.INVISIBLE);
            imgBorrar.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
