package com.example.calculadora;
//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DetectarInternet {
    private Context context;
    public  DetectarInternet (Context context){
        this.context = context;
    }

    public boolean hayConexion(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null){
                for (int i = 0; i < networkInfos.length; i++){
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
