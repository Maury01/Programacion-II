package com.example.calculadora;

public class usuarios {
    String Nombre, UserName, Correo, Password, token;
    public usuarios(){}

    public usuarios(String nombre, String userName, String correo, String password, String token) {
        Nombre = nombre;
        UserName = userName;
        Correo = correo;
        Password = password;
        this.token = token;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
