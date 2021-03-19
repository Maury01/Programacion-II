package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class actNuevoProducto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_nuevo_producto);
    }

    public void Atras (View view){
        Intent Regresar = new Intent(this, MainActivity.class);
        startActivity(Regresar);
    }
}