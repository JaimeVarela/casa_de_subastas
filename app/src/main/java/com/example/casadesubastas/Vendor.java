package com.example.casadesubastas;

import android.content.ContentValues;

import com.example.casadesubastas.database.TiendaVendedor;
import com.example.casadesubastas.database.TiendaVendedor.*;

public class Vendor {

    private int id;
    private String foto_uri;
    private String nombre;
    private String password;
    private double dinero;

    public Vendor(String foto_uri, String nombre, String password, double dinero) {
        this.foto_uri = foto_uri;
        this.nombre = nombre;
        this.password = password;
        this.dinero = dinero;
    }

    public Vendor(int id, String foto_uri, String nombre, String password, double dinero){
        this.id = id;
        this.foto_uri = foto_uri;
        this.nombre = nombre;
        this.password = password;
        this.dinero = dinero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoto_uri() {
        return foto_uri;
    }

    public void setFoto_uri(String foto_uri) {
        this.foto_uri = foto_uri;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getDinero() {
        return dinero;
    }

    public void setDinero(double dinero) {
        this.dinero = dinero;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(VendedorEntrada.FOTO_URI, foto_uri);
        values.put(VendedorEntrada.NOMBRE, nombre);
        values.put(VendedorEntrada.PASSWORD, password);
        values.put(VendedorEntrada.DINERO, dinero);
        return values;
    }
}
