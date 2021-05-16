package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Registro extends AppCompatActivity {
    EditText Nombre, Usuario, Correo, Password, ConfirmPassword;
    String NombreS, UsuarioS, CorreoS, PasswordS, ConfirmPasswordS, idUsuario, accion = "nuevo";
    TextView UserNOTValid;
    Button Atras, Registrarse;
    BD mIBD;
    DetectarInternet di;
    Cursor datosBDcursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Relacion XML-Java
        UserNOTValid = (TextView) findViewById(R.id.txtUsuarioNoValiable);
        Nombre = (EditText) findViewById(R.id.txtNombreReg);
        Usuario = (EditText) findViewById(R.id.txtUsuarioReg);
        Correo = (EditText) findViewById(R.id.txtCorreoReg);
        Password = (EditText) findViewById(R.id.txtPasswordReg);
        ConfirmPassword = (EditText) findViewById(R.id.txtConfirmPasswordReg);
        Atras = (Button) findViewById(R.id.btnAtrasReg);
        Registrarse = (Button) findViewById(R.id.btnRegistrarse);

        //Eventos
        Registrarse.setOnClickListener(v -> {
            RegistrarUsuario();
        });

        Atras.setOnClickListener(v -> {
            RegresarLogin();
        });
    }

    public void RegistrarUsuario(){
        try {
            mIBD = new BD(getApplicationContext(),"", null, 1);
            if (di.hayConexion()){
                Mensaje("Hay conexion Registro online");
            } else {Mensaje("Sin Conexion a Registro offline");}

            NombreS = Nombre.getText().toString();
            UsuarioS = Usuario.getText().toString();
            CorreoS = Correo.getText().toString();
            PasswordS = Password.getText().toString();
            ConfirmPasswordS = ConfirmPassword.getText().toString();

            if (NombreS != "" && UsuarioS != "" && CorreoS != "" && PasswordS != "" && ConfirmPasswordS != ""){
                if (PasswordS == ConfirmPasswordS){
                    String[] datos = {idUsuario, NombreS, CorreoS, UsuarioS, PasswordS};
                    datosBDcursor = mIBD.AministrarUsuarios("seleccionarUser", datos);

                    if (datosBDcursor.moveToFirst()){
                        UserNOTValid.setCursorVisible(true);
                    } else {
                        mIBD.AministrarUsuarios(accion, datos);
                        Mensaje("Registro Exitoso");
                    }
                } else { Mensaje("Las contraseñas no es igual a la confirmacion");}
            } else {Mensaje("Por favor llene todos los campos para continuar.");}

        } catch (Exception e){Mensaje("Resgistro User: " + e.getMessage());}
    }

    public void RegresarLogin(){
        Intent Regresar = new Intent(this, Login.class);
        startActivity(Regresar);
    }

    private void Mensaje(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}
