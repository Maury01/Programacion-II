
package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620
//id firebase picshared-a8ffb
public class MainActivity extends AppCompatActivity {
	Button IniciarSesion;
	GridView ListaImagenes;
	EditText Buscar;
	BD mibd;
	String Usuario = "-";
	DetectarInternet di;
	int position;
	ArrayList<Fotos> FotosArrayList = new ArrayList<Fotos>();
	ArrayList<Fotos> FotosArrayListCopy = new ArrayList<Fotos>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		IniciarSesion = (Button) findViewById(R.id.btnUsuario);
		ListaImagenes = (GridView) findViewById(R.id.grvImagenes);
		Buscar = (EditText) findViewById(R.id.txtBuscarMain);
		di = new DetectarInternet(getApplicationContext());



		BotonUsuario();

		IniciarSesion.setOnClickListener(v -> {
			AccionBotonUsuario();
		});
	}

	public void BotonUsuario(){
		try {
			if (Usuario == "-" || Usuario == null){
				IniciarSesion.setText("Iniciar Sesión");
				Mensaje("Inicie Sesion para Interacturar");
			} else if (Usuario != "-" || Usuario != null){
				IniciarSesion.setText("Mi cuenta");
				Usuario = getIntent().getStringExtra("User");
				Mensaje("Bienvenido " + Usuario);
			}
			Usuario = getIntent().getStringExtra("User");
		} catch (Exception e){ Mensaje("Error Boton User: " + e.getMessage());}
	}

	public void AccionBotonUsuario(){
		try {
			if (Usuario == "-" || Usuario == null){
				IrLogin();
			}else if (Usuario != "-" || Usuario == null){
				Micuenta();
			}
		} catch (Exception e){Mensaje("Error Accion btn Usuario: " + e.getMessage());}
	}

	public void Micuenta(){
		Intent micuenta = new Intent(this, MiPerfil.class);
		micuenta.putExtra("UserName", Usuario);
		startActivity(micuenta);
	}

	public void IrLogin(){
		Intent Logearse = new Intent(this, Login.class);
		startActivity(Logearse);
	}

	private void Mensaje(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	class Fotos {
		String Foto;

		public Fotos(String Foto){
			this.Foto = Foto;
		}
		public String getFoto(){
			return Foto;
		}
		public void setFoto(String idProductos) {
			this.Foto = Foto;
		}
	}
}
