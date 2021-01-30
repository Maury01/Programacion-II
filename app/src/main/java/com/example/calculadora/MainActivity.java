package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.IllegalFormatCodePointException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Calcular(View view){
        TextView TempVal = (TextView)findViewById(R.id.txtNum1);
        double Num1 = Double.parseDouble(TempVal.getText().toString());

        TempVal = (TextView)findViewById(R.id.txtNum2);
        double Num2 = Double.parseDouble(TempVal.getText().toString());

        double Respuesta = 1;

        Spinner cboOperaciones = findViewById(R.id.cboOperaciones);
        switch (cboOperaciones.getSelectedItemPosition()){
            case 0: //Suma
                Respuesta = Num1 +  Num2;
                break;

            case 1: //Resta
                Respuesta = Num1 - Num2;
                break;

            case 2: //Multip.
                Respuesta = Num1 * Num2;
                break;

            case 3: //Div.
                Respuesta = Num1 / Num2;
                break;

            case 4: //Factorial
                for (int i = 2; i <= Num1; i++){
                    Respuesta *=i;
                }
                break;

                case 5: //Porcentaje
                    Respuesta = (Num1 / Num2) * 100;
                    break;

            case 6: //Exponente
                Respuesta = Math.pow(Num1, Num2);
                break;

            case 7: //Raiz
                Respuesta = Math.pow(Num1, 1/Num2);
                break;

            case 8: //Mod
                Respuesta = Num1 % Num2;
                break;

            case 9: //Num mayor
                if (Num1 > Num2) {
                    Respuesta = Num1;
                } else  {
                    Respuesta = Num2;
                }
                break;
        }
        TempVal = findViewById(R.id.lblRespuesta);

        if (cboOperaciones.getSelectedItemPosition() == 5) {
            TempVal.setText("Respuesta: " + Respuesta + " %");
        } else {
            TempVal.setText("Respuesta" + Respuesta);
        }
    }
}