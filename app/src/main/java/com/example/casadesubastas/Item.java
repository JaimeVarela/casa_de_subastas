package com.example.casadesubastas;

import android.content.ContentValues;
import com.example.casadesubastas.database.TiendaProducto.*;

public class Item {
    private int id;
    private String foto_uri;
    private String nombre;
    private String descripcion;
    private String vendedor;
    private double precio;

    public Item(String foto_uri, String nombre, String descripcion, String vendedor, double precio) {
        this.foto_uri = foto_uri;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vendedor = vendedor;
        this.precio = precio;
    }

    public Item(int id, String foto_uri, String nombre, String descripcion, String vendedor, double precio){
        this.id = id;
        this.foto_uri = foto_uri;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vendedor = vendedor;
        this.precio = precio;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(TiendaEntrada.FOTO_URI, foto_uri);
        values.put(TiendaEntrada.NOMBRE, nombre);
        values.put(TiendaEntrada.DESCRIPCION, descripcion);
        values.put(TiendaEntrada.VENDEDOR, vendedor);
        values.put(TiendaEntrada.PRECIO, precio);
        return values;
    }
}
