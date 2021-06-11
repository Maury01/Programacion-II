package com.example.calculadora;

public class NombreUser {
    String UserLocal = "-";
    public void recibirNombreUser(String U){
        UserLocal = U;
    }
    public String EnviarNombreUser(){
        if (UserLocal != "-"){
            return UserLocal;
        }
        return UserLocal;
    }
    public void LimpiarNombreUser(){
        UserLocal = "-";
    }
}
