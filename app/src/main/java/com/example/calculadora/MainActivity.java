
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

public class MainActivity extends AppCompatActivity {
	FloatingActionButton btnRegistrar;
	BD miBD;
	ListView lstBuscar;
	Cursor datosPeliculasCursor = null;
	ArrayList<peliculas> peliculasArrayList = new ArrayList<peliculas>();
	ArrayList<peliculas> peliculasArrayListCopy =new ArrayList<peliculas>();
	peliculas mispeliculas;
	JSONArray JSONArrayDatosPeli;
	JSONObject JSONObjectDatosPeli;
	utilidades u = new utilidades();
	DetectarInternet di;
	int position = 0;
	String URLTrailer;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		di = new DetectarInternet(getApplicationContext());
		btnRegistrar = findViewById(R.id.btnRegistProducto);
		btnRegistrar.setOnClickListener(v -> {
			RegistrarPeliculas("nuevo");
		});
		//try {
			obteberDatosPeliculas();
			buscarPeliculas();
		//} catch (Exception e){mostrarMsgToask("On create main: " + e.getMessage());}
	}

	@Override //Se contruyé el menú.
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_productos, menu);
		try {
			AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
			position = adapterContextMenuInfo.position;
			menu.setHeaderTitle(JSONArrayDatosPeli.getJSONObject(position).getJSONObject("value").getString("Titulo"));

		} catch (Exception e){
			mostrarMsgToask("En context menu: "+e.getMessage());
		}

	}

	@Override //En esta sección se declararán las funciones del menu.
	public boolean onContextItemSelected(@NonNull MenuItem item) {
		try {
			switch (item.getItemId()) {
				case R.id.mnxAgregar:
					RegistrarPeliculas("nuevo");
					break;
				case R.id.mnxModificar:
					ModificarPeli("modificar");
					break;
				case R.id.mnxEliminar:
					eliminarPelicula();
					break;
				case R.id.mnxTrailer:
					VerTrailer();
					break;
			}
		} catch (Exception ex) {
			mostrarMsgToask("Context item: "+ex.getMessage());
		}
		return super.onContextItemSelected(item);
	}
	//Abrir pestaña de registro de productos
	private void RegistrarPeliculas(String accion) {
		try {
			Bundle parametrosProducts = new Bundle();
			parametrosProducts.putString("accion", accion);
			if(JSONArrayDatosPeli.length()>0){
				parametrosProducts.putString("datos", JSONArrayDatosPeli.getJSONObject(position).toString() );
			}

			Intent agregarProductos = new Intent(getApplicationContext(), actNuevoProducto.class);
			agregarProductos.putExtras(parametrosProducts);
			startActivity(agregarProductos);
		} catch (Exception e) {
			mostrarMsgToask("Registrar peli main: "+e.getMessage());
		}
	}

	//Eliminar un a peli
	private void eliminarPelicula() {
		try {
			AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
			confirmacion.setTitle("¿Desea eliminar la pelicula?");
			if (di.hayConexion()){
				JSONObjectDatosPeli = JSONArrayDatosPeli.getJSONObject(position).getJSONObject("value");
				confirmacion.setMessage(JSONObjectDatosPeli.getString("Titulo"));
			} else {
				confirmacion.setMessage(datosPeliculasCursor.getString(2));
			}
			confirmacion.setPositiveButton("Si", (dialog, which) -> {
				try{
					if (di.hayConexion()){
						ConexionServer objEliminarPeli = new ConexionServer();
						String resp = objEliminarPeli.execute(u.url_mto +
								JSONObjectDatosPeli.getString("_id") + "?_rev=" +
								JSONObjectDatosPeli.getString("_rev"), "DELETE").get();
						JSONObject jesonRespEliminar = new JSONObject(resp);
						if (jesonRespEliminar.getBoolean("ok")){
							JSONArrayDatosPeli.remove(position);
							mostrarDatosPeliculas();
						}
					} else {
						miBD = new BD(getApplicationContext(), "", null, 1);
						datosPeliculasCursor = miBD.administracionPeliculas("eliminar", new String[]{datosPeliculasCursor.getString(0)});
					}
					obteberDatosPeliculas();
					mostrarMsgToask("¡Registro Eliminado con exito!");
					dialog.dismiss();//cerrar el cuadro de dialogo
				} catch (Exception e){mostrarMsgToask("eliminar peli: "+e.getMessage());}

			});
			confirmacion.setNegativeButton("No", (dialog, which) -> {
				mostrarMsgToask("Eliminacion cancelada por el usuario...");
				dialog.dismiss();
			});
			confirmacion.create().show();
		} catch (Exception ex) {
			mostrarMsgToask("eliminar pelicula:"+ex.getMessage());
		}
	}

	private void obteberDatosPeliculasOnline(){
		try {
			ConexionServer conexionServer = new ConexionServer();
			String resp = conexionServer.execute(u.url_consulta, "GET").get();
			//mostrarMsgToask("resp: "+resp.toString());
			JSONObjectDatosPeli = new JSONObject(resp);
			JSONArrayDatosPeli = JSONObjectDatosPeli.getJSONArray("rows");
			mostrarDatosPeliculas();
		} catch (Exception e){mostrarMsgToask("datos online: "+e.getMessage());}
	}
	private void ObteberDatosPeliculasOffline(){
		try {
			miBD = new BD(getApplicationContext(), "", null, 1);
			datosPeliculasCursor = miBD.administracionPeliculas("seleccionar", null);

			if (datosPeliculasCursor.moveToFirst()){
				JSONArrayDatosPeli = new JSONArray();
				do {
					JSONObjectDatosPeli = new JSONObject();
					JSONObject jsonValueobject = new JSONObject();
					JSONObjectDatosPeli.put("_id", datosPeliculasCursor.getString(0));
					JSONObjectDatosPeli.put("_rev", datosPeliculasCursor.getString(0));
					JSONObjectDatosPeli.put("Codigo", datosPeliculasCursor.getString(1));
					JSONObjectDatosPeli.put("Titulo", datosPeliculasCursor.getString(2));
					JSONObjectDatosPeli.put("Sinopsis", datosPeliculasCursor.getString(3));
					JSONObjectDatosPeli.put("Duracion", datosPeliculasCursor.getString(4));
					JSONObjectDatosPeli.put("Precio", datosPeliculasCursor.getString(5));
					JSONObjectDatosPeli.put("URLFoto", datosPeliculasCursor.getString(6));
					JSONObjectDatosPeli.put("URLTrailer", datosPeliculasCursor.getString(7));
					jsonValueobject.put("value", JSONObjectDatosPeli);

					JSONArrayDatosPeli.put(jsonValueobject);
				} while (datosPeliculasCursor.moveToNext());
				mostrarDatosPeliculas();
			} else {
				mostrarMsgToask("No hay datos que mostrar, por favor agregue nuevos...");
				RegistrarPeliculas("nuevo");
			}
		} catch (Exception e){mostrarMsgToask("datos offline: "+e.getMessage());}
	}
	//Obtener los datos de la BD, más no mostrarlos.
	private void obteberDatosPeliculas() {
		if (di.hayConexion()){
			//mostrarMsgToask("Hay internet, mostrando datos de la nube");
			obteberDatosPeliculasOnline();
		} else {
			JSONArrayDatosPeli = new JSONArray();
			mostrarMsgToask("No hay internet, mostrando feed local");
			ObteberDatosPeliculasOffline();
		}
	}

	//Mostrar datros de la BD
	private void mostrarDatosPeliculas() {
		try {
			if (JSONArrayDatosPeli.length() > 0){
				lstBuscar = findViewById(R.id.ltsBuscar);
				peliculasArrayList.clear();
				peliculasArrayListCopy.clear();

				JSONObject jsonObject;

				for (int i = 0; i < JSONArrayDatosPeli.length(); i++){
					jsonObject = JSONArrayDatosPeli.getJSONObject(i).getJSONObject("value");
					mispeliculas = new peliculas(
							jsonObject.getString("_id"),
							jsonObject.getString("_rev"),
							jsonObject.getString("Codigo"),
							jsonObject.getString("Titulo"),
							jsonObject.getString("Sinopsis"),
							jsonObject.getString("Duracion"),
							jsonObject.getString("Precio"),
							jsonObject.getString("URLFoto"),
							jsonObject.getString("URLTrailer")
					);
					peliculasArrayList.add(mispeliculas);
				}
				adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), peliculasArrayList);
				lstBuscar.setAdapter(adaptadorImagenes);
				registerForContextMenu(lstBuscar);
				peliculasArrayListCopy.addAll(peliculasArrayList);
			} else {
				mostrarMsgToask("No hay registros para mostrar...");
			}
		} catch (Exception e){mostrarMsgToask("mostrar datos: "+e.getMessage());}
	}

	private void buscarPeliculas(){
		TextView Buscar = findViewById(R.id.txtBuscar);
		Buscar.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					peliculasArrayList.clear();
					if (Buscar.getText().toString().trim().length()<1){
						peliculasArrayList.addAll(peliculasArrayListCopy);
					} else {
						for (peliculas p : peliculasArrayListCopy){
							String Titulo = p.getCodigo();
							String Sinopsis = p.getSinopsis();


							String Buscando = Buscar.getText().toString().trim().toLowerCase();

							if (Titulo.toLowerCase().trim().contains(Buscando) || Sinopsis.toLowerCase().trim().contains(Buscando)){
								peliculasArrayList.add(p);
							}
						}
					}
					adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), peliculasArrayList);
					lstBuscar.setAdapter(adaptadorImagenes);
				} catch (Exception e){mostrarMsgToask("buscar: "+e.getMessage());}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void ModificarPeli(String accion){
		if(di.hayConexion()){
			try {
				Bundle parametros = new Bundle();
				parametros.putString("accion", accion);

				if(JSONArrayDatosPeli.length()>0){
					parametros.putString("datos", JSONArrayDatosPeli.getJSONObject(position).toString() );

				}

				Intent i = new Intent(getApplicationContext(), actNuevoProducto.class);
				i.putExtras(parametros);
				startActivity(i);

			}catch (Exception e){
				mostrarMsgToask("modificar: "+e.getMessage());
			}
		} else{

			try {
				Bundle parametros = new Bundle();
				parametros.putString("accion", accion);
				JSONObjectDatosPeli = new JSONObject();
				JSONObject jsonValueObject = new JSONObject();
				JSONArrayDatosPeli = new JSONArray();

				JSONObjectDatosPeli.put("_id", datosPeliculasCursor.getString(0));
				JSONObjectDatosPeli.put("_rev", datosPeliculasCursor.getString(0));
				JSONObjectDatosPeli.put("Codigo", datosPeliculasCursor.getString(1));
				JSONObjectDatosPeli.put("Titulo", datosPeliculasCursor.getString(2));
				JSONObjectDatosPeli.put("Sinopsis", datosPeliculasCursor.getString(3));
				JSONObjectDatosPeli.put("Duracion", datosPeliculasCursor.getString(4));
				JSONObjectDatosPeli.put("Precio", datosPeliculasCursor.getString(5));
				JSONObjectDatosPeli.put("URLFoto", datosPeliculasCursor.getString(6));
				JSONObjectDatosPeli.put("URLTrailer", datosPeliculasCursor.getString(7));
				jsonValueObject.put("value", JSONObjectDatosPeli);

				JSONArrayDatosPeli.put(jsonValueObject);

				if(JSONArrayDatosPeli.length()>0){
					parametros.putString("datos", JSONArrayDatosPeli.getJSONObject(position).toString() );
				}

				Intent i = new Intent(getApplicationContext(), actNuevoProducto.class);
				i.putExtras(parametros);
				startActivity(i);

			}catch (Exception e){
				mostrarMsgToask("modificar else: "+e.getMessage());
			}
		}
	}

	//Mostrar un mensaje
	private void mostrarMsgToask(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
	private class ConexionServer extends AsyncTask<String , String, String>{
		HttpURLConnection URLConnection;

		@Override
		protected void onPostExecute(String s){
			super.onPostExecute(s);
		}

		@Override
		protected String doInBackground(String... parametros) {
			StringBuilder result = new StringBuilder();
			try {
				String uri = parametros[0];
				String metodo =  parametros[1];
				URL url = new URL(uri);
				URLConnection = (HttpURLConnection)url.openConnection();
				URLConnection.setRequestMethod(metodo);

				InputStream inputStream = new BufferedInputStream(URLConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				String linea;
				while ((linea = bufferedReader.readLine()) != null){
					result.append(linea);
				}
			} catch (Exception e){
				Log.i("GET", "conexion server: "+e.getMessage());
			}
			return result.toString();
		}
	}
	public void VerTrailer(){
		Intent reproducir= new Intent(getApplicationContext(), Reproductor.class);
		startActivity(reproducir);
	}
}

	class peliculas {
		String idPelicula;
		String rev;
		String Codigo;
		String Titulo;
		String Sinopsis;
		String Duracion;
		String Precio;
		String URLFoto;
		String URLTrailer;

		public peliculas(String idPelicula, String rev, String Codigo, String Titulo, String Sinopsis, String Duracion, String Precio, String URLFoto, String URLTrailer) {
			this.idPelicula = idPelicula;
			this.rev = rev;
			this.Codigo = Codigo;
			this.Titulo = Titulo;
			this.Sinopsis = Sinopsis;
			this.Duracion = Duracion;
			this.Precio = Precio;
			this.URLFoto = URLFoto;
			this.URLTrailer = URLTrailer;
		}

		public String getIdPelicula(){
			return idPelicula;
		}
		public void setIdPelicula(String idPelicula) {
			this.idPelicula = idPelicula;
		}
		public String getRev(){
			return  rev;
		}
		public void setRev(String rev){
			this.rev = rev;
		}
		public String getCodigo(){
			return Codigo;
		}
		public void setCodigo(){
			this.Codigo = Codigo;
		}
		public String getTitulo(){
			return Titulo;
		}
		public void setTitulo(){
			this.Titulo = Titulo;
		}
		public String getSinopsis(){
			return Sinopsis;
		}
		public void setSinopsis(){
			this.Sinopsis = Sinopsis;
		}
		public String getDuracion(){
			return Duracion;
		}
		public void setPresentacion(){
			this.Duracion = Duracion;
		}
		public String getPrecio(){
			return Precio;
		}
		public void setPrecio(){
			this.Precio = Precio;
		}
		public String getURLFoto(){
			return URLFoto;
		}
		public void setURLFoto(){
			this.URLFoto = URLFoto;
		}
		public String getURLTrailer(){return URLTrailer;}
		public void setURLTrailer() {this.URLTrailer = URLTrailer;}
	}