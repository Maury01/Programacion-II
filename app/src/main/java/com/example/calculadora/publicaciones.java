package com.example.calculadora;

public class publicaciones {
String idPublicacion, Usuario, Descripcion, Categoria, URLFoto, URLFotoFirebase, token;

    public publicaciones() {
    }

    public publicaciones(String idPublicacion, String usuario, String descripcion, String categoria, String URLFoto, String URLFotoFirebase, String token) {
        this.idPublicacion = idPublicacion;
        this.Usuario = usuario;
        this.Descripcion = descripcion;
        this.Categoria = categoria;
        this.URLFoto = URLFoto;
        this.URLFotoFirebase = URLFotoFirebase;
        this.token = token;
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

    public String getURLFotoFirebase() { return URLFotoFirebase; }

    public void setURLFotoFirebase(String URLFotoFirebase) { this.URLFotoFirebase = URLFotoFirebase; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }
}
