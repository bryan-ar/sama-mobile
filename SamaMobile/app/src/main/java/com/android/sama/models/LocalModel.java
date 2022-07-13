package com.android.sama.models;

public class LocalModel {
    private int id;
    private String nombre;
    private String direccion;
    private double gps_lat;
    private double gps_lon;
    private int favorite;

    public LocalModel(int id, String nombre, String direccion, double gps_lat, double gps_lon, int favorite) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.gps_lat = gps_lat;
        this.gps_lon = gps_lon;
        this.favorite = favorite;
    }

    public double getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(double gps_lat) {
        this.gps_lat = gps_lat;
    }

    public double getGps_lon() {
        return gps_lon;
    }

    public void setGps_lon(double gps_lon) {
        this.gps_lon = gps_lon;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public LocalModel() {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "LocalModel{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
