package com.android.sama.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.sama.models.OfertaModel;
import com.android.sama.sqlite.DiagramaBD;
import com.android.sama.sqlite.ManageOpenHelper;

import java.util.ArrayList;

public class OfertaDAO {
    private ManageOpenHelper dbConexion;

    public OfertaDAO(Context context){
        dbConexion = new ManageOpenHelper(context);
    }

    public ArrayList<OfertaModel> listarOfertas(){
        ArrayList<OfertaModel> listaOfertas = new ArrayList<>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_OFERTAS, null );
        while(c.moveToNext()){
            listaOfertas.add(new OfertaModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3)
            ));
        }

        return listaOfertas;
    }

    public OfertaModel obtenerOferta(int id){
        OfertaModel oOferta = new OfertaModel();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_OFERTAS +
                " WHERE id = ?", new String[]{String.valueOf(id)} );
        while(c.moveToNext()){
            oOferta = new OfertaModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3)
            );
        }

        return oOferta;
    }
}
