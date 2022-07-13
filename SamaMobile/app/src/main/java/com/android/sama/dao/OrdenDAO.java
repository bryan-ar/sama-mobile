package com.android.sama.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.sama.models.PedidoOrdenModel;
import com.android.sama.sqlite.DiagramaBD;
import com.android.sama.sqlite.ManageOpenHelper;

import java.util.ArrayList;

public class OrdenDAO {
    private ManageOpenHelper dbConexion;

    public OrdenDAO(Context context){
        dbConexion = new ManageOpenHelper(context);
    }

    public ArrayList<PedidoOrdenModel> obtenerOrden(){
        ArrayList<PedidoOrdenModel> itemsOrdenPedido = new ArrayList<>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PEDIDOS_ORDEN, null );
        while(c.moveToNext()){
            itemsOrdenPedido.add(new PedidoOrdenModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getInt(3),
                    c.getDouble(4),
                    c.getDouble(5)
            ));
        }

        return itemsOrdenPedido;
    }

    public PedidoOrdenModel obtenerItem(int idProducto){
        PedidoOrdenModel itemsOrdenPedido = new PedidoOrdenModel();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PEDIDOS_ORDEN +
                " WHERE id_producto = ?",
                new String[]{
                        String.valueOf(idProducto)
                });
        while(c.moveToNext()){
            itemsOrdenPedido = new PedidoOrdenModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getInt(3),
                    c.getDouble(4),
                    c.getDouble(5)
            );
        }

        return itemsOrdenPedido;
    }

    public int insertarItem(PedidoOrdenModel oItem) {
        long id = 0;

        try {
            SQLiteDatabase db = dbConexion.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id_pedido", oItem.getIdPedido());
            cv.put("id_producto", oItem.getIdProducto());
            cv.put("nombre_producto", oItem.getNombreProducto());
            cv.put("cantidad", oItem.getCantidad());
            cv.put("precio", oItem.getPrecio());
            cv.put("monto_pagar", oItem.getMontoPagar());

            id = db.insert(DiagramaBD.TABLA_PEDIDOS_ORDEN, null, cv);
        } catch (Exception ex) {
            System.out.println("Error no pudo insertar datos.....insertarPedido......#######: " + ex.getMessage());
        }

        return (int)id;
    }

    public boolean actualizarItem(PedidoOrdenModel oItem){
        ContentValues cv = new ContentValues();
        cv.put("cantidad", oItem.getCantidad());
        cv.put("monto_pagar", oItem.getMontoPagar());

        SQLiteDatabase db = dbConexion.getWritableDatabase();
        return db.update(DiagramaBD.TABLA_PEDIDOS_ORDEN,
                cv,
                "id_producto=?",
                new String[]{
                        String.valueOf(oItem.getIdProducto())}) > 0;
    }

    public boolean eliminarItem(PedidoOrdenModel oItem){
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        return db.delete(DiagramaBD.TABLA_PEDIDOS_ORDEN,
                "id_producto=?",
                new String[]{
                        String.valueOf(oItem.getIdProducto())}) > 0;
    }
}
