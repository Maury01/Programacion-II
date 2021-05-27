package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registro extends AppCompatActivity {
    EditText Nombre, Usuario, Correo, Password, ConfirmPassword;
    String NombreS, UsuarioS, CorreoS, PasswordS, ConfirmPasswordS, idUsuario, accion = "nuevo";
    TextView UserNOTValid;
    Button Atras, Registrarse;
    BD mIBD;
    DetectarInternet di;
    Cursor datosBDcursor;
    DatabaseReference databaseReference;

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
            RegistrarUsuarioLocal();
            RegistrarUsuarioNube();
        });

        Atras.setOnClickListener(v -> {
            RegresarLogin();
        });
    }

    public void RegistrarUsuarioLocal(){
        try {
            mIBD = new BD(getApplicationContext(),"", null, 1);
            /*if (di.hayConexion()){
                Mensaje("Hay conexion Registro online");
            } else {Mensaje("Sin Conexion a Registro offline");}*/

            NombreS = Nombre.getText().toString();
            UsuarioS = Usuario.getText().toString();
            CorreoS = Correo.getText().toString();
            PasswordS = Password.getText().toString();
            ConfirmPasswordS = ConfirmPassword.getText().toString();

            if (NombreS != "" && UsuarioS != "" && CorreoS != "" && PasswordS != "" && ConfirmPasswordS != ""){
                if (PasswordS.equals(ConfirmPasswordS)){
                    String[] datos = {idUsuario, NombreS, CorreoS, UsuarioS, PasswordS};
                    //datosBDcursor = mIBD.AministrarUsuarios("seleccionarUser", datos);

                    /*if (datosBDcursor.moveToFirst()){
                        UserNOTValid.setCursorVisible(true);
                    } else {*/
                        mIBD.AministrarUsuarios(accion, datos);
                        Mensaje("Registro Exitoso");
                    //}
                } else { Mensaje("Las contraseñas no es igual a la confirmacion");}
            } else {Mensaje("Por favor llene todos los campos para continuar.");}

        } catch (Exception e){Mensaje("Resgistro User: " + e.getMessage());}
    }

    public void RegistrarUsuarioNube(){
        try {
            if (NombreS != "" && UsuarioS != "" && CorreoS != "" && PasswordS != "" && ConfirmPasswordS != ""){
                if (PasswordS.equals(ConfirmPasswordS)){
                    databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
                } else { Mensaje("Las contraseñas no es igual a la confirmacion");}
            } else {Mensaje("Por favor llene todos los campos para continuar.");}
        } catch (Exception e){Mensaje("Registro nube: " + e.getMessage());}
    }

    public void RegresarLogin(){
        Intent Regresar = new Intent(this, Login.class);
        startActivity(Regresar);
    }

    private void Mensaje(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}
