
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
    Spinner cboDe, cboA;
	ConversoresArea MiConversor = new ConversoresArea();

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

        btnCalculo = (Button) findViewById(R.id.btnCalcularArea);
		btnCalculo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					Temp = (TextView) findViewById(R.id.txtCantidadArea);
					double cantidad = Double.parseDouble(Temp.getText().toString());

					cboDe = findViewById(R.id.cboDeArea);
					cboA = findViewById(R.id.cboAArea);
					Temp = findViewById(R.id.lblRespuestaArea);
					double roundOff = Math.round((MiConversor.Convertir(cboDe.getSelectedItemPosition(), cboA.getSelectedItemPosition(), cantidad))*10000.0) / 10000.0;
					Temp.setText("Respuesta: " + roundOff);
				}catch (Exception e){
					Temp = findViewById(R.id.lblRespuestaArea);
					Temp.setText("Por favor ingrese los valores correspondiente");
					Toast.makeText(getApplicationContext(), "Por ingrese los valores correspondiente "+ e.getMessage(),Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
}

class ConversoresArea {
	double [] conversiones = {1, 0.1111, 0.111111,0.092903,  0.00014774656489, 0.000013188960818, 0.0000092903};//Area;
	public double Convertir(int de, int a, double cantidad){
		return(conversiones[a] / conversiones[de] * cantidad);
	}
}