package com.example.calculadora;

public class publicaciones {
String idPublicacion, Usuario, Descripcion, Categoria, URLFoto;

    public publicaciones() {
    }

    public publicaciones(String idPublicacion, String usuario, String descripcion, String categoria, String URLFoto) {
        this.idPublicacion = idPublicacion;
        Usuario = usuario;
        Descripcion = descripcion;
        Categoria = categoria;
        this.URLFoto = URLFoto;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getURLFoto() {
        return URLFoto;
    }

    public void setURLFoto(String URLFoto) {
        this.URLFoto = URLFoto;
    }
}
