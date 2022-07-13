package com.android.sama.models;

public class OfertaModel {
    private int id;
    private String nombre;
    private String descripcion;
    private double monto_descuento;

    public OfertaModel(int id, String nombre, String descripcion, double monto_descuento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto_descuento = monto_descuento;
    }

    public OfertaModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMontoDescuento() {
        return monto_descuento;
    }

    public void setMontoDescuento(double monto_descuento) {
        this.monto_descuento = monto_descuento;
    }
}
