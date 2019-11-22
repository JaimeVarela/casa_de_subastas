package com.example.casadesubastas.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.casadesubastas.database.TiendaProducto.*;
import com.example.casadesubastas.database.TiendaVendedor.*;

public class ListaDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CasaDeSubastas.db";

    public ListaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TiendaEntrada.TABLA_NAME + "(" +
                TiendaEntrada.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TiendaEntrada.FOTO_URI + " TEXT, " +
                TiendaEntrada.NOMBRE + " TEXT NOT NULL, " +
                TiendaEntrada.DESCRIPCION + " TEXT, " +
                TiendaEntrada.VENDEDOR + " TEXT NOT NULL, " +
                TiendaEntrada.PRECIO + " DOUBLE)");

        db.execSQL("CREATE TABLE " + VendedorEntrada.TABLA_NAME + "(" +
                VendedorEntrada.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VendedorEntrada.FOTO_URI + " TEXT, " +
                VendedorEntrada.NOMBRE + " TEXT NOT NULL UNIQUE, " +
                VendedorEntrada.PASSWORD + " TEXT NOT NULL, " +
                VendedorEntrada.DINERO + " DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TiendaEntrada.TABLA_NAME);
        onCreate(db);
    }
}
