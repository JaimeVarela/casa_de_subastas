package com.example.casadesubastas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.casadesubastas.Vendor;
import com.example.casadesubastas.database.TiendaVendedor.*;

import java.util.ArrayList;

public class VendorDatabase {

    private Context context;
    private ListaDbHelper dbHelper;

    public VendorDatabase(Context context){
        this.context = context;
        dbHelper = new ListaDbHelper(context);
    }

    public long insertar(Vendor vendor){
        //ListaDbHelper dbHelper = new ListaDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.insert(
                VendedorEntrada.TABLA_NAME,
                null,
                vendor.toContentValues());
    }

    public long modificar(ContentValues vendor, int i){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.update(
                VendedorEntrada.TABLA_NAME,
                vendor,
                VendedorEntrada.ID + " = " + i,
                null
        );
    }

    public long borrar(int i){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                VendedorEntrada.TABLA_NAME,
                VendedorEntrada.ID + " = " + i,
                null
        );
    }

    public ArrayList<Vendor> buscar(){
        ArrayList<Vendor> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + VendedorEntrada.TABLA_NAME, null);
        while (c.moveToNext()){
            Vendor vendor = new Vendor(
                    c.getInt(0),//id
                    c.getString(1),//foto_uri,
                    c.getString(2),//nombre,
                    c.getString(3),//password,
                    c.getDouble(4)//dinero
            );
            list.add(vendor);
        }
        c.close();
        db.close();

        return list;
    }

    public Vendor buscar_usuario(String nombre, String password){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + VendedorEntrada.TABLA_NAME, null);
        while (c.moveToNext()){
            //Nombre y contraseña coincide con un elemento de la base de datos
            if(c.getString(2).equals(nombre) && c.getString(3).equals(password)){
                Vendor vendor = new Vendor(
                        c.getInt(0),//id
                        c.getString(1),//foto_uri
                        c.getString(2),//nombre
                        c.getString(3),//password
                        c.getDouble(4)//dinero
                );
                c.close();
                db.close();
                return vendor;
            }
            //Nombre encontrado pero contraseña incorrecta
            else if(c.getString(2).equals(nombre) && !c.getString(3).equals(password)){
                c.close();
                db.close();
                return null;
            }
        }
        //Usuario no encontrado
        c.close();
        db.close();
        return null;
    }

    public void sumar_dinero(String nombre, double dinero){
        //Buscar el usuario
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + VendedorEntrada.TABLA_NAME, null);
        while (c.moveToNext()){
            //Nombre coincide
            if(c.getString(2).equals(nombre)){
                Vendor vendor = new Vendor(
                        c.getInt(0),//id
                        c.getString(1),//foto_uri
                        c.getString(2),//nombre
                        c.getString(3),//password
                        c.getDouble(4)//dinero
                );
                c.close();
                db.close();

                vendor.setDinero(vendor.getDinero() + dinero); //Sumar el dinero
                modificar(vendor.toContentValues(), vendor.getId()); //Modificar en la base de datos
                return;
            }
        }
        //Usuario no encontrado (No debería pasar)
        c.close();
        db.close();
    }
}
