package com.android.sama.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.sama.models.PedidoModel;
import com.android.sama.sqlite.DiagramaBD;
import com.android.sama.sqlite.ManageOpenHelper;

import java.util.ArrayList;

public class PedidoDAO {
    private ManageOpenHelper dbConexion;

    public PedidoDAO(Context context){
        dbConexion = new ManageOpenHelper(context);
    }

    public ArrayList<PedidoModel> listarPedidos(){
        ArrayList<PedidoModel> listaPedidos = new ArrayList<>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PEDIDOS, null );
        while(c.moveToNext()){
            listaPedidos.add(new PedidoModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getDouble(4),
                    c.getInt(5),
                    c.getDouble(6),
                    c.getString(7)
            ));
        }

        return listaPedidos;
    }

    public PedidoModel obtenerPedido(int idPedido){
        PedidoModel oPedido = new PedidoModel();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PEDIDOS +
                " WHERE id = ?", new String[]{String.valueOf(idPedido)} );
        while(c.moveToNext()){
            oPedido = new PedidoModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getDouble(4),
                    c.getInt(5),
                    c.getDouble(6),
                    c.getString(7)
            );
        }

        return oPedido;
    }

    public PedidoModel obtenerPedidoUsuario(int idUsuario){
        PedidoModel oPedido = new PedidoModel();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+  DiagramaBD.TABLA_PEDIDOS +
                " WHERE id_usuario = ?", new String[]{String.valueOf(idUsuario)} );
        while(c.moveToNext()){
            oPedido = new PedidoModel(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getDouble(4),
                    c.getInt(5),
                    c.getDouble(6),
                    c.getString(7)
            );
        }

        return oPedido;
    }

    public int insertarPedido(PedidoModel oPedido) {
        long id = 0;

        try {
            SQLiteDatabase db = dbConexion.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", oPedido.getId());
            cv.put("id_usuario", oPedido.getIdUsuario());
            cv.put("fecha_creacion", oPedido.getFechaCreacion());
            cv.put("descripcion", oPedido.getDescripcion());
            cv.put("monto_total", oPedido.getMonto_total());
            cv.put("estado", oPedido.getEstado());
            cv.put("calificacion", oPedido.getCalificacion());
            cv.put("hora_llegada", oPedido.getHoraLlegada());

            id = db.insert(DiagramaBD.TABLA_PEDIDOS, null, cv);
        } catch (Exception ex) {
            System.out.println("Error no pudo insertar datos.....insertarPedido......#######: " + ex.getMessage());
        }

        return (int)id;
    }

    public boolean actualizarPedido(PedidoModel oPedido){
        ContentValues cv = new ContentValues();
        cv.put("id_usuario", oPedido.getIdUsuario());
        cv.put("fecha_creacion", oPedido.getFechaCreacion());
        cv.put("descripcion", oPedido.getDescripcion());
        cv.put("monto_total", oPedido.getMonto_total());
        cv.put("estado", oPedido.getEstado());
        cv.put("calificacion", oPedido.getCalificacion());
        cv.put("hora_llegada", oPedido.getHoraLlegada());

        SQLiteDatabase db = dbConexion.getWritableDatabase();
        return db.update(DiagramaBD.TABLA_PEDIDOS,
                cv,
                "id=?",
                new String[]{String.valueOf(oPedido.getId())}) > 0;
    }

    public boolean calificarPedido(PedidoModel oPedido){
        ContentValues cv = new ContentValues();
        cv.put("id_usuario", oPedido.getIdUsuario());
        cv.put("estado", oPedido.getEstado());
        cv.put("calificacion", oPedido.getCalificacion());

        SQLiteDatabase db = dbConexion.getWritableDatabase();
        return db.update(DiagramaBD.TABLA_PEDIDOS,
                cv,
                "id=?",
                new String[]{String.valueOf(oPedido.getId())}) > 0;
    }

    public boolean eliminarPedido(int id){
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        return db.delete(DiagramaBD.TABLA_PEDIDOS,
                "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }
}
