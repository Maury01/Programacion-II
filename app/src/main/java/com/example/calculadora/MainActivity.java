
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
	EditText Buscar, U;
	BD mibd;
	String Usuario = "-";
	DetectarInternet di;
	int position;
	DatabaseReference databaseReference;
	publicaciones post;
	JSONArray datosJSONArray = new JSONArray();
	JSONObject datosJSONObject;
	String miToken;
	ArrayList<publicaciones> FotosArrayList = new ArrayList<publicaciones>();
	ArrayList<publicaciones> FotosArrayListCopy = new ArrayList<publicaciones>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		IniciarSesion = (Button) findViewById(R.id.btnUsuario);
		ListaImagenes = (GridView) findViewById(R.id.grvImagenes);
		Buscar = (EditText) findViewById(R.id.txtBuscarMain);
		di = new DetectarInternet(getApplicationContext());
		/*U = (EditText) findViewById(R.id.txtUsurioLogin);
		if (U.getText() != null){
			Usuario = U.getText().toString();
		}*/
		Usuario = "Mauricionk";


		try {
			BotonUsuario();
			mostrarPosts();
		} catch (Exception e){Mensaje("cargar main: " + e.getMessage());}

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
				//Usuario = getIntent().getStringExtra("User");
				Mensaje("Bienvenido " + Usuario);
			}
			Usuario = getIntent().getStringExtra("User");
		} catch (Exception e){ Mensaje("Error Boton User: " + e.getMessage());}
	}

	public void AccionBotonUsuario(){
		try {
			//if (Usuario == "-" || Usuario == null){
			//	IrLogin();
			//}else if (Usuario != "-" || Usuario != null){
				Micuenta();
			//}
		} catch (Exception e){Mensaje("Error Accion btn Usuario: " + e.getMessage());}
	}

	public void mostrarPosts(){
		databaseReference = FirebaseDatabase.getInstance().getReference("Publicaciones");

		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				try {
					for (DataSnapshot dataSnapshot : snapshot.getChildren()){
						post = dataSnapshot.getValue(publicaciones.class);
						FotosArrayList.add(post);

						datosJSONObject = new JSONObject();
						datosJSONObject.put("Categoria", post.getCategoria());
						datosJSONObject.put("Descripccion", post.getDescripcion());
						datosJSONObject.put("key", post.getIdPublicacion());
						datosJSONObject.put("URL", post.getURLFoto());
						datosJSONObject.put("URLOnline", post.getURLFotoFirebase());
						datosJSONObject.put("User", post.getUsuario());
						datosJSONArray.put(datosJSONObject);
					}
					adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), FotosArrayList);
					ListaImagenes.setAdapter(adaptadorImagenes);

				} catch (Exception e){Mensaje("Mostrar posts: " + e.getMessage());}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	public void Micuenta(){
		Intent micuenta = new Intent(this, MiPerfil.class);
		//micuenta.putExtra("UserName", Usuario);
		startActivity(micuenta);
	}

	public void IrLogin(){
		Intent Logearse = new Intent(this, Login.class);
		startActivity(Logearse);
	}

	private void Mensaje(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}


}
