package com.jesus.waiapp;


public class Blog {

    private String title;
    private String desc;
    private String image;
    private String fecha;
    private String genero;
    private String descripcion;
    private String nombreUsuario;
    private String precioAlquiler;
    private String fotoPerfil;
    private String nombreJuego;


    public Blog(){}

    public Blog(String title, String desc, String image, String fecha, String genero, String descripcion, String nombreUsuario, String precioAlquiler, String fotoPerfil, String nombreJuego) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.fecha = fecha;
        this.genero = genero;
        this.descripcion = descripcion;
        this.nombreUsuario = nombreUsuario;
        this.precioAlquiler = precioAlquiler;
        this.fotoPerfil = fotoPerfil;
        this.nombreJuego = nombreJuego;


    }


    public String getTitle() {
        return title;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getFecha() {
        return fecha;
    }
    public String getGenero() {
        return genero;
    }
    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public String getPrecioAlquiler() {
        return precioAlquiler;
    }
    public String getFotoPerfil() {
        return fotoPerfil;
    }
    public String getNombreJuego() {
        return nombreJuego;
    }




}
