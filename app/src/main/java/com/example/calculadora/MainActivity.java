
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
	RelativeLayout Contenido;
    TextView Temp;
    Button btnCalculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Contenido = findViewById(R.id.contenidoView);
		tbhPestanas = findViewById(R.id.tbhParcial);
        tbhPestanas.setup();

		tbhPestanas.addTab(tbhPestanas.newTabSpec("Agua").setContent(R.id.tabMetrosAgua).setIndicator("Agua"));
        tbhPestanas.addTab(tbhPestanas.newTabSpec("Area").setContent(R.id.tabArea).setIndicator("Area"));

		btnCalculo = (Button) findViewById(R.id.btnCalcular);
        btnCalculo.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View V){
					CalcularAgua();
				}

				private void CalcularAgua() {
					try {
						double Procedimiento;
						Temp = (TextView) findViewById(R.id.txtCantidad);
						double Cantidad = Double.parseDouble(Temp.getText().toString());
						Temp = (TextView) findViewById((R.id.lblPago));

						if (Cantidad > 0 && Cantidad < 19){
							Temp.setText("Monto a pagar: $6.00");
						} else if (Cantidad > 18 && Cantidad <29) {
							Procedimiento = ((Cantidad - 18) * 0.45) + 6;
							Temp.setText("Monto a pagar: $" + Procedimiento);
						} else {
							Procedimiento = (((Cantidad - 28)*0.65)+(10*0.45))+6;
							Temp.setText("Monto a pagar: $" + Procedimiento);
						}

					} catch (Exception e) {
						Temp = findViewById(R.id.lblPago);
						Temp.setText("Por favor rellene el campo correctamente");
					}
				}
        });
    }
}
