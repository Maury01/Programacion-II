package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

public class Login extends AppCompatActivity {
    EditText Correo, Password;
    String accion = "seleccionar", CorreoS, PasswordS, idUsuario = "", Usuario = "-";
    Button Atras, IniciarSesion, Registrarse;
    Cursor datosBDCursor;
    DetectarInternet di;
    BD miBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Relacion XML-Java
        Correo = (EditText) findViewById(R.id.txtCorreoLogin);
        Password = (EditText) findViewById(R.id.txtPasswordLogin);
        Atras = (Button) findViewById(R.id.btnAtrasLogin);
        Registrarse = (Button) findViewById(R.id.btnRegistraseLogin);
        IniciarSesion = (Button) findViewById(R.id.btnIniciarSesionLogin);

        //Eventos
        Atras.setOnClickListener(v -> {
            PaginaPrincipal();
        });

        Registrarse.setOnClickListener(v -> {
            Registrarse();
        });

        IniciarSesion.setOnClickListener(v -> {
            iniciarsesion();
        });
    }

    public void iniciarsesion(){
        /*if (di.hayConexion()){
            Mensaje("Hay conexion iniciar sesion Online");
        } else {Mensaje("Sin Conexion iniciar sesion con la feed offline");}*/

        try {
            miBD = new BD(getApplicationContext(), "", null, 1);

            CorreoS = Correo.getText().toString();
            PasswordS = Password.getText().toString();

            if (CorreoS != "" && PasswordS != ""){
              String[] datos = {idUsuario, CorreoS, PasswordS, Usuario};
              datosBDCursor= miBD.AministrarUsuarios("seleccionar", datos);
              if (datosBDCursor.moveToFirst()){
                  datosBDCursor.getString(3); //Usuario
                  Usuario = datosBDCursor.toString();
                  Mensaje("Bienvenido " + Usuario);

                  PaginaPrincipal();
              } else {Mensaje("Correo o Contraseña incorrectos");}
            } else {Mensaje("Por favor, Llene todos los campos");}
        } catch (Exception e){Mensaje("Error iniciar sesion: " + e.getMessage());}
    }

    public void PaginaPrincipal(){
        Intent PagPrincipal = new Intent(this, MainActivity.class);
        PagPrincipal.putExtra("User", Usuario);
        startActivity(PagPrincipal);
    }

    public void Registrarse(){
        Intent Reg = new Intent(this, Registro.class);
        startActivity(Reg);
    }

    private void Mensaje(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
}