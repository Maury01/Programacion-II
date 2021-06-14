package com.example.calculadora;

public class comentarios {
        String idComentarios, Usaurio, Texto, idPublicacion;

    public comentarios(){}

    public comentarios(String idComentarios, String usaurio, String texto, String idPublicacion) {
        this.idComentarios = idComentarios;
        Usaurio = usaurio;
        Texto = texto;
        this.idPublicacion = idPublicacion;
    }

    public String getIdComentarios() {
        return idComentarios;
    }

    public void setIdComentarios(String idComentarios) {
        this.idComentarios = idComentarios;
    }

    public String getUsaurio() {
        return Usaurio;
    }

    public void setUsaurio(String usaurio) {
        Usaurio = usaurio;
    }

    public String getTexto() {
        return Texto;
    }

    public void setTexto(String texto) {
        Texto = texto;
    }

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }
}
