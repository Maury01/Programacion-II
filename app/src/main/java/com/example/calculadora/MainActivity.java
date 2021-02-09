package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.IllegalFormatCodePointException;

public class MainActivity extends AppCompatActivity {
    TabHost tbhConversores;
    Button btnCalcular;
    TextView tempVal;
    Spinner cboDe, cboA;
    Conversores MiConversor = new Conversores();
    RelativeLayout contenidoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contenidoView = findViewById(R.id.contenidoView);
        tbhConversores = findViewById(R.id.tbhConversores);
        tbhConversores.setup();

        tbhConversores.addTab(tbhConversores.newTabSpec("$↔€").setContent(R.id.tabMonedas));
        tbhConversores.addTab(tbhConversores.newTabSpec("Tiempo").setContent(R.id.tabTiempo));

        btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                try {
                    tempVal = (TextView) findViewById(R.id.txtCantidad);
                    double Cantidad =  Double.parseDouble(tempVal.getText().toString());

                    cboDe = findViewById(R.id.cboDe);
                    cboA = findViewById(R.id.cboA);

                    tempVal = findViewById(R.id.lblRespuesta);
                    tempVal.setText("" + MiConversor.Convertir(0, cboDe.getSelectedItemPosition(), cboA.getSelectedItemPosition(), Cantidad));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese solo valores validos " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCalcular = findViewById(R.id.btnCalcularTiempo);
        btnCalcular.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                try {
                    tempVal = (TextView) findViewById(R.id.txtCantTiempo);
                    double Cantidad =  Double.parseDouble(tempVal.getText().toString());

                    cboDe = findViewById(R.id.cboDeTiempo);
                    cboA = findViewById(R.id.cboATiempo);

                    tempVal = findViewById(R.id.lblRespuestaTiempo);
                    tempVal.setText("" + MiConversor.Convertir(1, cboDe.getSelectedItemPosition(), cboA.getSelectedItemPosition(), Cantidad));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese solo valores validos " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

class Conversores {
    double [][] conversiones = {
            {1.0, 1.27, 8.75, 105.12, 0.83, 7.77, 24.30, 3.64, 20.10, 3571.0}, //Monedas
            {1.0, 0.001, 0.000001, 0.000000016666667, 0.000000000277778, 0.000000000011574, 0.000000000001653, 0.00000000000038052, 0.000000000000032, 0.000000000000003171, 3.171e-16},//Tiempo
            {1.0}
    };
    public double Convertir(int opcion, int de, int a, double cantidad){
        return conversiones[opcion][a] / conversiones[opcion][de] * cantidad;
    }
}