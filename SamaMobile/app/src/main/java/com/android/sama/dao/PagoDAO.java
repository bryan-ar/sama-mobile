package com.android.sama.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.sama.models.PedidoPagoModel;
import com.android.sama.sqlite.DiagramaBD;
import com.android.sama.sqlite.ManageOpenHelper;

public class PagoDAO {
    private ManageOpenHelper dbConexion;

    public PagoDAO(Context context){
        dbConexion = new ManageOpenHelper(context);
    }

    public int insertarPago(PedidoPagoModel oPago) {
        long id = 0;

        try {
            SQLiteDatabase db = dbConexion.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", oPago.getId());
            cv.put("id_pedido", oPago.getIdPedido());
            cv.put("tarjeta", oPago.getTarjeta());
            cv.put("nombre", oPago.getNombre());
            cv.put("fecha_exp", oPago.getFechaExpiracion());
            cv.put("cvv", oPago.getCvv());
            cv.put("estado", oPago.getEstado());

            id = db.insert(DiagramaBD.TABLA_PEDIDOS_PAGOS, null, cv);
        } catch (Exception ex) {
            System.out.println("Error no pudo insertar datos.....insertarPago......#######: " + ex.getMessage());
        }

        return (int)id;
    }

    public PedidoPagoModel obtenerPago(){
        PedidoPagoModel pago = new PedidoPagoModel();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PEDIDOS_PAGOS, null);
        while(c.moveToNext()){
            pago = new PedidoPagoModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getInt(6)
            );
        }

        return pago;
    }
}
