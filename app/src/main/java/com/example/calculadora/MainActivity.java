
package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.IllegalFormatCodePointException;

public class MainActivity extends AppCompatActivity {
    TabHost tbhPestanas;
    EditText txtCant;
    TextView lblResultado;
    Button btnCalculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbhPestanas = findViewById(R.id.tbhParcial);
        tbhPestanas.setup();
        txtCant.findViewById(R.id.txtCantidad);
        lblResultado.findViewById(R.id.lblPago);

        tbhPestanas.addTab(tbhPestanas.newTabSpec("Agua").setContent(R.id.tabMetrosAgua).setIndicator("Agua"));
        tbhPestanas.addTab(tbhPestanas.newTabSpec("Area").setContent(R.id.tabArea).setIndicator("Area"));

        btnCalculo.findViewById(R.id.btnCalcular);
        btnCalculo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View V){
                int PrecioBase = 6;
                double Cantidad = Double.parseDouble(txtCant.getText().toString());

                if (Cantidad > 0 && Cantidad <= 18){
                    lblResultado.setText("Monto a pagar: $" + PrecioBase);
                }
            }
        });
    }
}