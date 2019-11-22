package com.example.casadesubastas.database;

public class TiendaProducto {

    public static abstract class TiendaEntrada{
        public static final String TABLA_NAME = "Productos";

        public static final String ID = "id";
        public static final String FOTO_URI = "foto_uri";
        public static final String NOMBRE = "nombre";
        public static final String DESCRIPCION = "descripcion";
        public static final String VENDEDOR = "vendedor";
        public static final String PRECIO = "precio";
    }
}
