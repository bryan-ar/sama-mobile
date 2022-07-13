package com.android.sama.models;

import java.util.ArrayList;

public class PedidoModel {
    private int id;
    private int id_usuario;
    private String fecha_creacion;
    private String descripcion;
    private double monto_total;
    private int estado;
    private double calificacion;
    private String hora_llegada;
    public ArrayList<PedidoOrdenModel> orden;
    public PedidoPagoModel pago;

    public PedidoModel(int id, int id_usuario, String fecha_creacion, String descripcion, double monto_total, int estado, double calificacion, String hora_llegada) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.fecha_creacion = fecha_creacion;
        this.descripcion = descripcion;
        this.monto_total = monto_total;
        this.estado = estado;
        this.calificacion = calificacion;
        this.hora_llegada = hora_llegada;
        orden = new ArrayList<>();
        pago = new PedidoPagoModel();
    }

    public PedidoModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.id_usuario = idUsuario;
    }

    public String getFechaCreacion() {
        return fecha_creacion;
    }

    public void setFechaCreacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(double monto_total) {
        this.monto_total = monto_total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getHoraLlegada() {
        return hora_llegada;
    }

    public void setHoraLlegada(String hora_llegada) {
        this.hora_llegada = hora_llegada;
    }

    @Override
    public String toString() {
        return "PedidoModel{" +
                "id=" + id +
                ", id_usuario=" + id_usuario +
                ", fecha_creacion='" + fecha_creacion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", monto_total=" + monto_total +
                ", estado=" + estado +
                ", calificacion=" + calificacion +
                ", hora_llegada='" + hora_llegada + '\'' +
                '}';
    }
}
