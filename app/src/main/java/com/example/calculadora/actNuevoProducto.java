package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class actNuevoProducto extends AppCompatActivity {

    BD miBD;
    TextView TempVal;
    Intent TomarFotoIntent;
    Button RegistrarProdct, atras;
    ImageView imgProducto;
    String URL, idProducto, accion = "nuevo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_nuevo_producto);

        miBD = new BD(getApplicationContext(),"",null, 1);

        atras = findViewById(R.id.btnRegresar);
        atras.setOnClickListener(v -> {
            Atras();
        });

        imgProducto = findViewById(R.id.imgFoto);
        imgProducto.setOnClickListener(v -> {
            //Aqui se llama la parte de tomar foto
        });

        RegistrarProdct = findViewById(R.id.btnRegistrar);
        RegistrarProdct.setOnClickListener(v -> {
            try {
                TempVal = findViewById(R.id.txtCodigo);
                String Codigo = TempVal.getText().toString();

                TempVal = findViewById(R.id.txtDescripcion);
                String Descripcion = TempVal.getText().toString();

                TempVal = findViewById(R.id.txtMarca);
                String Marca = TempVal.getText().toString();

                TempVal = findViewById(R.id.txtPresentacion);
                String Presentacion = TempVal.getText().toString();

                TempVal = findViewById(R.id.txtPrecio);
                String Precio = TempVal.getText().toString();

                String[] datos = {idProducto, Codigo, Descripcion, Marca, Presentacion, Precio, URL};
                miBD.administracionProductos(accion,datos);

                mostrarMsgToast("Registro guardado con exito");
                Atras();
            } catch (Exception e){e.getMessage();}

        });
    }

    public void Atras (){
        Intent Regresar = new Intent(this, MainActivity.class);
        startActivity(Regresar);
    }

    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
}