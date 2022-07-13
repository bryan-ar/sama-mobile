package com.android.sama.models;

public class ProductoModel {
    private int id;
    private int id_categoria;
    private String descripcion;
    private double precio;
    private int stock;

    public ProductoModel(int id, int id_categoria, String descripcion, double precio, int stock) {
        this.id = id;
        this.id_categoria = id_categoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public ProductoModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return id_categoria;
    }

    public void setIdCategoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
