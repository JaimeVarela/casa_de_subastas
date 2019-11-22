package com.example.casadesubastas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.casadesubastas.Item;
import com.example.casadesubastas.database.TiendaProducto.*;

import java.util.ArrayList;

public class ItemDatabase {

    private Context context;
    private ListaDbHelper dbHelper;

    public ItemDatabase(Context context){
        this.context = context;
        dbHelper = new ListaDbHelper(context);
    }

    public long insertar(Item item){
        //ListaDbHelper dbHelper = new ListaDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.insert(
                TiendaEntrada.TABLA_NAME,
                null,
                item.toContentValues());
    }

    public long modificar(ContentValues item, int i){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.update(
                TiendaEntrada.TABLA_NAME,
                item,
                "ID = " + i,
                null
        );
    }

    public long borrar(int i){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                TiendaEntrada.TABLA_NAME,
                "ID = " + i,
                null
        );
    }

    public ArrayList<Item> buscar(){
        ArrayList<Item> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TiendaEntrada.TABLA_NAME, null);
        while (c.moveToNext()){
            Item item = new Item(
                    c.getInt(0),//id
                    c.getString(1),//foto_uri,
                    c.getString(2),//nombre,
                    c.getString(3),//descripcion,
                    c.getString(4),//vendedor,
                    c.getDouble(5)//precio
            );
            list.add(item);
        }
        c.close();
        db.close();

        return list;
    }
}
