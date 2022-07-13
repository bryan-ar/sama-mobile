package com.android.sama.models;

public class PedidoPagoModel {
    private int id;
    private int id_pedido;
    private String tarjeta;
    private String nombre;
    private String fecha_exp;
    private String cvv;
    private int estado;

    public PedidoPagoModel(int id, int id_pedido, String tarjeta, String nombre, String fecha_exp, String cvv, int estado) {
        this.id = id;
        this.id_pedido = id_pedido;
        this.tarjeta = tarjeta;
        this.nombre = nombre;
        this.fecha_exp = fecha_exp;
        this.cvv = cvv;
        this.estado = estado;
    }

    public PedidoPagoModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return id_pedido;
    }

    public void setIdPedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaExpiracion() {
        return fecha_exp;
    }

    public void setFechaExpiracion(String fecha_exp) {
        this.fecha_exp = fecha_exp;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
