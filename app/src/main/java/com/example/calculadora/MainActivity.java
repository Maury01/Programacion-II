
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

        tbhConversores = findViewById(R.id.tbhConversores);
        tbhConversores.setup();

        tbhConversores.addTab(tbhConversores.newTabSpec("Divisas").setContent(R.id.tabDivisas).setIndicator("Divisas"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Tiempo").setContent(R.id.tabTiempo).setIndicator("Tiempo"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Almacenamiento").setContent(R.id.tabAlmacenamiento).setIndicator("Almacenamiento"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Longitud").setContent(R.id.tabLongitud).setIndicator("Longitud"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Masa").setContent(R.id.tabMasa).setIndicator("Masa"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Temperatura").setContent(R.id.tabTemperatura).setIndicator("Temperatura"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Volumen").setContent(R.id.tabVolumen).setIndicator("volumen"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Area").setContent(R.id.tabArea).setIndicator("Area"));


        btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                try {
                    tempVal = (TextView) findViewById(R.id.txtcantidad);
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
                    tempVal = (TextView) findViewById(R.id.txtCantidadTiempo);
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

        btnCalcular = findViewById(R.id.btnCalcularAlmacenamineto);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tempVal = findViewById(R.id.txtCantidadA);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());

                    cboDe = findViewById(R.id.cboDeAAlmacenamiento);
                    cboA = findViewById(R.id.cboAAlmacenamiento);
                    tempVal = findViewById(R.id.lblRespuestaAlmacenamiento);

                    tempVal.setText("Respuesta: " + MiConversor.Convertir(2, cboDe.getSelectedItemPosition(), cboA.getSelectedItemPosition(), cantidad));
                }catch (Exception e){
                    tempVal = findViewById(R.id.lblRespuestaL);
                    tempVal.setText("Por favor ingrese los valores correspondiente");
                    Toast.makeText(getApplicationContext(), "Por ingrese los valores correspondiente "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCalcular = findViewById(R.id.btnCalcularLongitud); //boton calcularlongitus
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    tempVal = (TextView) findViewById(R.id.txtCantidadLongitud);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());

                    cboDe = findViewById(R.id.cboDeLongitud);
                    cboA = findViewById(R.id.cboALongitud);
                    tempVal = findViewById(R.id.lblRespuestaL);
                    double roundOff = Math.round((MiConversor.Convertir(3, cboDe.getSelectedItemPosition(), cboA.getSelectedItemPosition(), cantidad))*10000.0) / 10000.0;
                    tempVal.setText("Respuesta: " + roundOff);
                }catch (Exception e){
                    tempVal = findViewById(R.id.lblRespuestaL);
                    tempVal.setText("Por favor ingrese los valores correspondiente");
                    Toast.makeText(getApplicationContext(), "Por ingrese los valores correspondiente "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCalcular = findViewById(R.id.btnCalcularMasa); //boton calcularMasa
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    tempVal = (TextView) findViewById(R.id.txtCantidadMasa);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());

                    cboDe = findViewById(R.id.cboDeMasa);
                    cboA = findViewById(R.id.cboAMasa);
                    tempVal = findViewById(R.id.lblRespuestaMasa);
                    double roundOff = Math.round((MiConversor.Convertir(4, cboDe.getSelectedItemPosition(), cboA.getSelectedItemPosition(), cantidad))*10000.0) / 10000.0;
                    tempVal.setText("Respuesta: " + roundOff);
                }catch (Exception e){
                    tempVal = findViewById(R.id.lblRespuestaMasa);
                    tempVal.setText("Por favor ingrese los valores correspondiente");
                    Toast.makeText(getApplicationContext(), "Por ingrese los valores correspondiente "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCalcular = findViewById(R.id.btnCalcularTemperatura); //boton calcularTemperatura
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    tempVal = (TextView) findViewById(R.id.txtCantidadTemperatura);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    double Respuesta = 0;
                    cboDe = findViewById(R.id.cboDeTemperatura);
                    cboA = findViewById(R.id.cboATemperatura);
                    tempVal = findViewById(R.id.lblRespuestaTemperatura);
                    if (cboDe.getSelectedItemPosition() == 0){
                        if (cboA.getSelectedItemPosition() == 0) {
                            Respuesta = cantidad;
                        }
                        if (cboA.getSelectedItemPosition() == 1){
                            Respuesta =  Math.round((cantidad * 1.8) + 32);
                        }
                        if (cboA.getSelectedItemPosition() == 2){
                            Respuesta =  Math.round(cantidad + 273.15);
                        }
                    }
                    if (cboDe.getSelectedItemPosition() == 1) {
                        if (cboA.getSelectedItemPosition() == 0){
                            Respuesta = ((cantidad - 32) * (9/5));
                        }
                        if (cboA.getSelectedItemPosition() == 1){
                            Respuesta = cantidad;
                        }
                        if (cboA.getSelectedItemPosition() == 2){
                            Respuesta = (cantidad - 32) * (9/5) + 273.15;
                        }
                    }
                    if (cboDe.getSelectedItemPosition() == 2){
                        if (cboA.getSelectedItemPosition() == 0){
                            Respuesta = Math.round(cantidad - 273.15);
                        }
                        if (cboA.getSelectedItemPosition() == 1){
                            Respuesta = Math.round(((cantidad - 273.15) * (9/5)) + 32);
                        }
                        if (cboA.getSelectedItemPosition() == 2){
                            Respuesta = cantidad;
                        }
                    }

                    tempVal.setText("Respuesta: " + Respuesta);
                }catch (Exception e){
                    tempVal = findViewById(R.id.lblRespuestaTemperatura);
                    tempVal.setText("Por favor ingrese los valores correspondiente");
                    Toast.makeText(getApplicationContext(), "Por ingrese los valores correspondiente "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

class Conversores {
    double [][] conversiones = {
            {1.0, 1.27, 8.75, 105.12, 0.83, 7.77, 24.30, 3.64, 20.10, 3571.0}, //Monedas
            {1.0, 0.001, 0.000001, 0.000000016666667, 0.000000000277778, 0.000000000011574, 0.000000000001653, 0.00000000000038052, 0.000000000000032, 0.000000000000003171, 3.171e-16},//Tiempo
            {1.0, 0.000977, 0.000000953, 0.000000000931, 0.000000000000909, 0.000000000000000888, 0.000000000000000000867, 0.000000000000000000000847, 0.000000000000000000000001, 0.000000000000000000000000827},//Almacenamiento
            {1.0, 0.1,0.01, 0.001, 0.000001,0.00001, 0.039370, 0.0032808, 0.00109, 0.000000621371}, //Longitud
            {1,0.1,0.01,0.001,0.0001,0.00001,0.000001,0.000035274,2.2046e-6,0.000000001},//Masa
            {1,33.8,273.15}//Temperatura
    };
    public double Convertir(int opcion, int de, int a, double cantidad){
        return Math.round((conversiones[opcion][a] / conversiones[opcion][de] * cantidad));
    }
}