package com.android.sama.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ManageOpenHelper extends SQLiteOpenHelper {

    public ManageOpenHelper(Context context) {
        super(context, DiagramaBD.DB_NAME, null, DiagramaBD.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DiagramaBD.CreateTablePedidos);
            db.execSQL(DiagramaBD.CreateTablePedidosOrden);
            db.execSQL(DiagramaBD.CreateTablePagos);
            db.execSQL(DiagramaBD.CreateTableProductos);
            db.execSQL(DiagramaBD.CreateTableLocales);
            db.execSQL(DiagramaBD.CreateTableOfertas);
        }catch (SQLException ex){
            System.out.println("Error, no se crearon las tablas Sqlite......");
            throw ex;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(DiagramaBD.DropTablePedidos);
            db.execSQL(DiagramaBD.DropTablePedidosOrden);
            db.execSQL(DiagramaBD.DropTablePagos);
            db.execSQL(DiagramaBD.DropTableProductos);
            db.execSQL(DiagramaBD.DropTableLocales);
            db.execSQL(DiagramaBD.DropTableOfertas);

            db.execSQL(DiagramaBD.CreateTablePedidos);
            db.execSQL(DiagramaBD.CreateTablePedidosOrden);
            db.execSQL(DiagramaBD.CreateTablePagos);
            db.execSQL(DiagramaBD.CreateTableProductos);
            db.execSQL(DiagramaBD.CreateTableLocales);
            db.execSQL(DiagramaBD.CreateTableOfertas);
        }catch (SQLException ex){
            System.out.println("Error no se actualizaron las tablas Sqlite......");
            throw ex;
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

    }

    public void cleanBD() {
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(DiagramaBD.DropTablePedidos);
            db.execSQL(DiagramaBD.DropTablePedidosOrden);
            db.execSQL(DiagramaBD.DropTablePagos);
            db.execSQL(DiagramaBD.DropTableProductos);
            db.execSQL(DiagramaBD.DropTableLocales);
            db.execSQL(DiagramaBD.DropTableOfertas);

            db.execSQL(DiagramaBD.CreateTablePedidos);
            db.execSQL(DiagramaBD.CreateTablePedidosOrden);
            db.execSQL(DiagramaBD.CreateTablePagos);
            db.execSQL(DiagramaBD.CreateTableProductos);
            db.execSQL(DiagramaBD.CreateTableLocales);
            db.execSQL(DiagramaBD.CreateTableOfertas);
        }catch (SQLException ex){
            System.out.println("Error no se pudo limpiar la BD Sqlite......");
            throw ex;
        }
    }

    public void cleanProductosBD() {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(DiagramaBD.DropTableProductos);
            db.execSQL(DiagramaBD.CreateTableProductos);

        }catch (SQLException ex){
            System.out.println("Error no se pudo limpiar la BD Sqlite......");
            throw ex;
        }

    }

}
