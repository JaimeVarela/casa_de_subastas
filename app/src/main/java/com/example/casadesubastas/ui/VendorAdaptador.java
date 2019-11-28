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
import com.example.casadesubastas.Vendor;

import java.util.ArrayList;

public class VendorAdaptador extends BaseAdapter {
    private Context context;
    private ArrayList<Vendor> listVendors;

    public VendorAdaptador(Context context, ArrayList<Vendor> listVendors) {
        this.context = context;
        this.listVendors = listVendors;
    }

    @Override
    public int getCount() {
        return listVendors.size();
    }

    @Override
    public Object getItem(int position) {
        return listVendors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listVendors.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Vendor vendor = (Vendor)getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.vendor_main, null);

        TextView nombre = (TextView)convertView.findViewById(R.id.vendedor_nombre);
        TextView password = (TextView)convertView.findViewById(R.id.vendedor_password);
        TextView dinero = (TextView)convertView.findViewById(R.id.vendedor_dinero);

        nombre.setText(vendor.getNombre());
        password.setText(vendor.getPassword());
        dinero.setText(String.valueOf(vendor.getDinero()));

        ImageView imgEdit = (ImageView)convertView.findViewById(R.id.item_editar);
        ImageView imgBorrar = (ImageView)convertView.findViewById(R.id.item_borrar);

        return convertView;
    }
}
