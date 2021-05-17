package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MiPerfil extends AppCompatActivity {

    TextView prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        prueba = (TextView) findViewById(R.id.txtprueba);
        String texto;
        texto = getIntent().getStringExtra("UserName");
        prueba.setText("@" + texto);
    }
}