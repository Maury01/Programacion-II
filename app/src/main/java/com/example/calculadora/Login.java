package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

public class Login extends AppCompatActivity {
    EditText Usuario, Password;
    String accion = "seleccionar", PasswordS, mitoken, UsuarioS, User = "-";
    Button Atras, IniciarSesion, Registrarse;
    Cursor datosBDCursor;
    DetectarInternet di;
    BD miBD;
    DatabaseReference databaseReference;
    usuarios u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Relacion XML-Java
        Usuario = (EditText) findViewById(R.id.txtUsurioLogin);
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
            //miBD = new BD(getApplicationContext(), "", null, 1);


                databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
            if (databaseReference != null) {
                UsuarioS = Usuario.toString();
                PasswordS = Password.getText().toString();
                databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (PasswordS.equals(u.getPassword())) {
                            User = UsuarioS;
                            Mensaje(User);
                            PaginaPrincipal();
                        } else {
                            Mensaje("Contraseña incorrecta");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {Mensaje("F");}
        } catch (Exception e){Mensaje("Error iniciar sesion: " + e.getMessage());}
    }
    public void verificarContraseña(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void PaginaPrincipal(){
        Intent PagPrincipal = new Intent(this, MainActivity.class);
        PagPrincipal.putExtra("User", User);
        startActivity(PagPrincipal);
    }

    public void Registrarse(){
        Intent Reg = new Intent(this, Registro.class);
        startActivity(Reg);
    }

    private void Mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}