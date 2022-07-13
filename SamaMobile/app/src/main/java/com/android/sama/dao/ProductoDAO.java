package com.android.sama.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.sama.models.ProductoModel;
import com.android.sama.sqlite.DiagramaBD;
import com.android.sama.sqlite.ManageOpenHelper;

import java.util.ArrayList;

public class ProductoDAO {
    private ManageOpenHelper dbConexion;

    public ProductoDAO(Context context){
        dbConexion = new ManageOpenHelper(context);
    }

    public ProductoDAO(Context context, boolean limpiarProductos){
        dbConexion = new ManageOpenHelper(context);
        if(limpiarProductos){
            dbConexion.cleanProductosBD();
        }
    }

    public ArrayList<ProductoModel> listarProductos(int idCategoria){
        ArrayList<ProductoModel> listaProductos = new ArrayList<>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PRODUCTOS +
            " WHERE id_categoria = ?", new String[]{String.valueOf(idCategoria)} );
        while(c.moveToNext()){
            listaProductos.add(new ProductoModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getDouble(3),
                    c.getInt(4)
            ));
        }

        return listaProductos;
    }

    public ProductoModel obtenerProducto(int id){
        ProductoModel oProducto = new ProductoModel();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PRODUCTOS +
                " WHERE id = ?", new String[]{String.valueOf(id)} );
        while(c.moveToNext()){
            oProducto = new ProductoModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getDouble(3),
                    c.getInt(4)
            );
        }

        return oProducto;
    }

    public int insertarProducto(ProductoModel oProducto) {
        long id = 0;

        try {
            SQLiteDatabase db = dbConexion.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", oProducto.getId());
            cv.put("id_categoria", oProducto.getIdCategoria());
            cv.put("descripcion", oProducto.getDescripcion());
            cv.put("precio", oProducto.getPrecio());
            cv.put("stock", oProducto.getStock());

            id = db.insert(DiagramaBD.TABLA_PRODUCTOS, null, cv);
        } catch (Exception ex) {
            System.out.println("Error no pudo insertar datos.....insertarProducto......#######: " + ex.getMessage());
        }

        return (int)id;
    }
}
