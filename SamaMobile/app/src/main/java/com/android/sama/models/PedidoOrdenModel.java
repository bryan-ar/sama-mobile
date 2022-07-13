package com.android.sama.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PedidoOrdenModel {
    private int id_pedido;
    private int id_producto;
    private String nombre_producto;
    private int cantidad;
    private double precio;
    private double monto_pagar;

    public PedidoOrdenModel(int id_pedido, int id_producto, String nombre_producto, int cantidad, double precio, double monto_pagar) {
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.monto_pagar = monto_pagar;
    }

    public PedidoOrdenModel() {
    }

    public int getIdPedido() {
        return id_pedido;
    }

    public void setIdPedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getIdProducto() {
        return id_producto;
    }

    public void setIdProducto(int idProducto) {
        this.id_producto = id_producto;
    }

    public String getNombreProducto() {
        return nombre_producto;
    }

    public void setNombreProducto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void restar() {
        this.cantidad--;
        calcularMontoTotal();
    }

    public void aumentar() {
        this.cantidad++;
        calcularMontoTotal();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getMontoPagar() {
        return monto_pagar;
    }

    public void setMontoPagar(double monto_pagar) {
        this.monto_pagar = monto_pagar;
    }

    public void calcularMontoTotal(){
        this.monto_pagar = this.cantidad * this.precio;
        BigDecimal bd = new BigDecimal(this.monto_pagar).setScale(2, RoundingMode.HALF_DOWN);
        this.monto_pagar = bd.doubleValue();
    }

    @Override
    public String toString() {
        return "PedidoOrdenModel{" +
                "idPedido=" + id_pedido +
                ", idProducto=" + id_producto +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", montoPagar=" + monto_pagar +
                '}';
    }
}
