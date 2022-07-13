package com.android.sama.sqlite;

public class DiagramaBD {
    public static final String DB_NAME = "healthy";
    public static final int DB_VERSION = 1;

    public static final String NOMBRE_DB = "sama.db";
    public static final String TABLA_PEDIDOS = "pedidos";
    public static final String TABLA_PEDIDOS_ORDEN = "pedidos_orden";
    public static final String TABLA_PEDIDOS_PAGOS = "pedidos_pagos";
    public static final String TABLA_PRODUCTOS = "productos";
    public static final String TABLA_LOCALES = "locales";
    public static final String TABLA_OFERTAS = "ofertas";

    //Creación de tablas
    public static final String CreateTablePedidos =
            "CREATE TABLE " + TABLA_PEDIDOS +
            "(id INTEGER, "+
            " id_usuario INTEGER," +
            " fecha_creacion TEXT, "+
            " descripcion TEXT," +
            " monto_total REAL," +
            " estado INTEGER," +
            " calificacion FLOAT," +
            " hora_llegada TEXT);";

    public static final String CreateTablePedidosOrden =
            "CREATE TABLE " + TABLA_PEDIDOS_ORDEN +
                    "(id_pedido INTEGER, "+
                    " id_producto INTEGER," +
                    " nombre_producto TEXT," +
                    " cantidad INTEGER, "+
                    " precio REAL," +
                    " monto_pagar REAL);";

    public static final String CreateTablePagos =
            "CREATE TABLE " + TABLA_PEDIDOS_PAGOS +
                    "(id INTEGER, "+
                    " id_pedido INTEGER," +
                    " tarjeta TEXT, "+
                    " nombre TEXT," +
                    " fecha_exp TEXT," +
                    " cvv TEXT," +
                    " estado INTEGER);";

    public static final String CreateTableProductos =
            "CREATE TABLE " + TABLA_PRODUCTOS +
                    "(id INTEGER, "+
                    " id_categoria INTEGER," +
                    " descripcion TEXT," +
                    " precio REAL," +
                    " stock INTEGER);";

    public static final String CreateTableLocales =
            "CREATE TABLE " + TABLA_LOCALES +
                    "(id INTEGER, "+
                    " nombre TEXT, "+
                    " direccion TEXT, "+
                    " gps_lat REAL," +
                    " gps_lon REAL," +
                    " favorite INTEGER);";

    public static final String CreateTableOfertas =
            "CREATE TABLE " + TABLA_OFERTAS +
                    "(id INTEGER, "+
                    " nombre TEXT, "+
                    " descripcion TEXT, "+
                    " monto_descuento REAL); ";

    /********************************************************************/
    /********************************************************************/
    /********************************************************************/

    public static final String DropTablePedidos =
            "DROP TABLE IF EXISTS " + TABLA_PEDIDOS + ";";

    public static final String DropTablePedidosOrden =
            "DROP TABLE IF EXISTS " + TABLA_PEDIDOS_ORDEN + ";";

    public static final String DropTablePagos =
            "DROP TABLE IF EXISTS " + TABLA_PEDIDOS_PAGOS + ";";

    public static final String DropTableProductos =
            "DROP TABLE IF EXISTS " + TABLA_PRODUCTOS + ";";

    public static final String DropTableLocales =
            "DROP TABLE IF EXISTS " + TABLA_LOCALES + ";";

    public static final String DropTableOfertas =
            "DROP TABLE IF EXISTS " + TABLA_OFERTAS + ";";
}
