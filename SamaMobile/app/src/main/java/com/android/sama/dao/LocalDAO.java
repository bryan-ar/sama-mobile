package com.android.sama.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.sama.models.LocalModel;
import com.android.sama.sqlite.DiagramaBD;
import com.android.sama.sqlite.ManageOpenHelper;

import java.util.ArrayList;

public class LocalDAO {
    private ManageOpenHelper dbConexion;

    public LocalDAO(Context context){
        dbConexion = new ManageOpenHelper(context);
    }

    public ArrayList<LocalModel> listarLocales(){
        ArrayList<LocalModel> listaLocales = new ArrayList<>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_LOCALES, null );
        while(c.moveToNext()){
            listaLocales.add(new LocalModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3),
                    c.getDouble(4),
                    c.getInt(5)
            ));
        }

        return listaLocales;
    }

    public LocalModel obtenerLocal(int id){
        LocalModel oOferta = new LocalModel();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_LOCALES +
                " WHERE id = ?", new String[]{String.valueOf(id)} );
        while(c.moveToNext()){
            oOferta = new LocalModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3),
                    c.getDouble(4),
                    c.getInt(5)
            );
        }

        return oOferta;
    }
}
