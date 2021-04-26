
package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.AbstractCursor;
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

public class MainActivity extends AppCompatActivity {
	FloatingActionButton btnRegistrar;
	BD miBD;
	ListView lstBuscar;
	Cursor datosProductosCursor = null;
	ArrayList<productos> productosArrayList = new ArrayList<productos>();
	ArrayList<productos> productosArrayListCopy=new ArrayList<productos>();
	productos misproductos;
	JSONArray JSONArrayDatosProduct;
	JSONObject JSONObjectDatosProduct;
	utilidades u = new utilidades();
	DetectarInternet di;
	int position = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		di = new DetectarInternet(getApplicationContext());
		btnRegistrar = findViewById(R.id.btnRegistProducto);
		btnRegistrar.setOnClickListener(v -> {
			RegistrarProduct("nuevo");
		});
		try {
			obteberDatosProductos();
			buscarProducto();
		} catch (Exception e){mostrarMsgToask("On create main: " + e.getMessage());}
	}

	@Override //Se contruyé el menú.
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_productos, menu);
		try {
			if (di.hayConexion()){
				AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
				position = adapterContextMenuInfo.position;
				menu.setHeaderTitle(JSONArrayDatosProduct.getJSONObject(position).getJSONObject("value").getString("Descripcion"));
			} else {
				AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
				datosProductosCursor.moveToPosition(adapterContextMenuInfo.position);

				menu.setHeaderTitle(datosProductosCursor.getString(2));
			}

		} catch (Exception e){
			mostrarMsgToask("En context menu: "+e.getMessage());
		}

	}

	@Override //En esta sección se declararán las funciones del menu.
	public boolean onContextItemSelected(@NonNull MenuItem item) {
		try {
			switch (item.getItemId()) {
				case R.id.mnxAgregar:
					RegistrarProduct("nuevo");
					break;
				case R.id.mnxModificar:
					ModificarProduct ("modificar");
					break;
				case R.id.mnxEliminar:
					eliminarProducto();
					break;
			}
		} catch (Exception ex) {
			mostrarMsgToask("Context item: "+ex.getMessage());
		}
		return super.onContextItemSelected(item);
	}
	//Abrir pestaña de registro de productos
	private void RegistrarProduct(String accion) {
		try {
			Bundle parametrosProducts = new Bundle();
			parametrosProducts.putString("accion", accion);

			Intent agregarProductos = new Intent(getApplicationContext(), actNuevoProducto.class);
			agregarProductos.putExtras(parametrosProducts);
			startActivity(agregarProductos);
		} catch (Exception e) {
			mostrarMsgToask("Registrar prod main: "+e.getMessage());
		}
	}

	//Eliminar un producto.
	private void eliminarProducto() {
		try {
			AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
			confirmacion.setTitle("¿Desea eliminar el siguiente producto?");
			if (di.hayConexion()){
				JSONObjectDatosProduct = JSONArrayDatosProduct.getJSONObject(position).getJSONObject("value");
				confirmacion.setMessage(JSONObjectDatosProduct.getString("Descripcion"));
			} else {
				confirmacion.setMessage(datosProductosCursor.getString(2));
			}
			confirmacion.setPositiveButton("Si", (dialog, which) -> {
				try{
					if (di.hayConexion()){
						ConexionServer objEliminarProduct = new ConexionServer();
						String resp = objEliminarProduct.execute(u.url_mto +
								JSONObjectDatosProduct.getString("_id") + "?_rev=" +
								JSONObjectDatosProduct.getString("_rev"), "DELETE").get();
						JSONObject jesonRespEliminar = new JSONObject(resp);
						if (jesonRespEliminar.getBoolean("ok")){
							JSONArrayDatosProduct.remove(position);
							mostrarDatosProductos();
						}
					} else {
						miBD = new BD(getApplicationContext(), "", null, 1);
						datosProductosCursor = miBD.administracionProductos("eliminar", new String[]{datosProductosCursor.getString(0)});
					}
					obteberDatosProductos();
					mostrarMsgToask("¡Registro Eliminado con exito!");
					dialog.dismiss();//cerrar el cuadro de dialogo
				} catch (Exception e){mostrarMsgToask("eliminar prod: "+e.getMessage());}

			});
			confirmacion.setNegativeButton("No", (dialog, which) -> {
				mostrarMsgToask("Eliminacion cancelada por el usuario...");
				dialog.dismiss();
			});
			confirmacion.create().show();
		} catch (Exception ex) {
			mostrarMsgToask("eliminar prod:"+ex.getMessage());
		}
	}

	private void obteberDatosProductosOnline(){
		try {
			ConexionServer conexionServer = new ConexionServer();
			String resp = conexionServer.execute(u.url_consulta, "GET").get();
			mostrarMsgToask("resp: "+resp.toString());
			JSONObjectDatosProduct = new JSONObject(resp);
			JSONArrayDatosProduct = JSONObjectDatosProduct.getJSONArray("rows");
			mostrarDatosProductos();
		} catch (Exception e){mostrarMsgToask("datos online: "+e.getMessage());}
	}
	private void ObteberDatosProductosOffline(){
		try {
			miBD = new BD(getApplicationContext(), "", null, 1);
			datosProductosCursor = miBD.administracionProductos("seleccionar", null);

			if (datosProductosCursor.moveToFirst()){
				mostrarDatosProductos();
			} else {
				mostrarMsgToask("No hay datos que mostrar, por favor agregue nuevos...");
				RegistrarProduct("nuevo");
			}
		} catch (Exception e){mostrarMsgToask("datos offline: "+e.getMessage());}
	}
	//Obtener los datos de la BD, más no mostrarlos.
	private void obteberDatosProductos() {
		if (di.hayConexion()){
			mostrarMsgToask("Hay internet, mostrando datos de la nube");
			obteberDatosProductosOnline();
		} else {
			//JSONArrayDatosProduct = new JSONArray();
			mostrarMsgToask("No hay internet, mostrando feed local");
			ObteberDatosProductosOffline();
		}
	}

	//Mostrar datros de la BD
	private void mostrarDatosProductos() {
		try {
			lstBuscar = findViewById(R.id.ltsBuscar);
			productosArrayList.clear();
			productosArrayListCopy.clear();
			JSONObject jsonObject;
			if (di.hayConexion()){
				if (JSONArrayDatosProduct.length() > 0){
					for (int i = 0; i < JSONArrayDatosProduct.length(); i++){
						jsonObject = JSONArrayDatosProduct.getJSONObject(i).getJSONObject("value");
						misproductos = new productos(
								jsonObject.getString("_id"),
								jsonObject.getString("_rev"),
								jsonObject.getString("Codigo"),
								jsonObject.getString("Descripcion"),
								jsonObject.getString("Marca"),
								jsonObject.getString("Presentacion"),
								jsonObject.getString("Precio"),
								jsonObject.getString("URLFoto")
						);
						productosArrayList.add(misproductos);
					}
				} else {
					mostrarMsgToask("No hay registros que mostrar...");
					RegistrarProduct("nuevo");
				}
			}  else {
				do {
					misproductos = new productos(
							datosProductosCursor.getString(0),//idproducto
							datosProductosCursor.getString(1),//codigo
							datosProductosCursor.getString(1),//codigo
							datosProductosCursor.getString(2),//descripcion
							datosProductosCursor.getString(3),//marca
							datosProductosCursor.getString(4),//presentacion
							datosProductosCursor.getString(5), //precio
							datosProductosCursor.getString(6) //urldefoto
					);
					productosArrayListCopy.add(misproductos);
				} while (datosProductosCursor.moveToNext());
			}
			
			adaptadorImagenes adaptadorimagenes = new adaptadorImagenes(getApplicationContext(), productosArrayList);
			mostrarMsgToask("adaptador: "+ adaptadorimagenes.toString());
			lstBuscar.setAdapter(adaptadorimagenes);
			registerForContextMenu(lstBuscar);
			productosArrayListCopy.addAll(productosArrayList);
		} catch (Exception e){mostrarMsgToask("mostrar datos: "+e.getMessage());}
	}

	private void buscarProducto(){
		TextView Buscar = findViewById(R.id.txtBuscar);
		Buscar.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					productosArrayList.clear();
					if (Buscar.getText().toString().trim().length()<1){
						productosArrayList.addAll(productosArrayListCopy);
					} else {
						for (productos p : productosArrayListCopy){
							String Codigo = p.getCodigo();
							String Descripcion = p.getDescripcion();
							String Marca = p.getMarca();
							String Presentacion = p.getPresentacion();
							String Precio = p.getPrecio();

							String Buscando = Buscar.getText().toString().trim().toLowerCase();

							if (Descripcion.toLowerCase().trim().contains(Buscando) || Descripcion.toLowerCase().trim().contains(Buscando) ||
								Marca.toLowerCase().trim().contains(Buscando) || Presentacion.toLowerCase().trim().contains(Buscando) ||
								Presentacion.toLowerCase().trim().contains(Buscando) || Precio.toLowerCase().trim().contains(Buscando)){
								productosArrayList.add(p);
							}
						}
					}
					adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), productosArrayList);
					lstBuscar.setAdapter(adaptadorImagenes);
				} catch (Exception e){mostrarMsgToask("buscar: "+e.getMessage());}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void ModificarProduct(String accion){
		if(di.hayConexion()){
			try {
				Bundle parametros = new Bundle();
				parametros.putString("accion", accion);

				if(JSONArrayDatosProduct.length()>0){
					parametros.putString("datos", JSONArrayDatosProduct.getJSONObject(position).toString() );

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
				JSONObjectDatosProduct = new JSONObject();
				JSONObject jsonValueObject = new JSONObject();
				JSONArrayDatosProduct = new JSONArray();

				JSONObjectDatosProduct.put("_id", datosProductosCursor.getString(0));
				JSONObjectDatosProduct.put("_rev", datosProductosCursor.getString(0));
				JSONObjectDatosProduct.put("Codigo", datosProductosCursor.getString(1));
				JSONObjectDatosProduct.put("Descripcion", datosProductosCursor.getString(2));
				JSONObjectDatosProduct.put("Marca", datosProductosCursor.getString(3));
				JSONObjectDatosProduct.put("Presentacion", datosProductosCursor.getString(4));
				JSONObjectDatosProduct.put("Precio", datosProductosCursor.getString(5));
				JSONObjectDatosProduct.put("URLFoto", datosProductosCursor.getString(6));
				jsonValueObject.put("value", JSONObjectDatosProduct);

				JSONArrayDatosProduct.put(jsonValueObject);

				if(JSONArrayDatosProduct.length()>0){
					parametros.putString("datos", JSONArrayDatosProduct.getJSONObject(position).toString() );

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
}


	class productos {
		String idProductos;
		String rev;
		String Codigo;
		String Descripcion;
		String Marca;
		String Presentacion;
		String Precio;
		String URLFoto;


		public productos(String idProductos, String rev, String Codigo, String Descripcion, String Marca, String Presentacion, String Precio, String URLFoto) {
			this.idProductos = idProductos;
			this.rev = rev;
			this.Codigo = Codigo;
			this.Descripcion = Descripcion;
			this.Marca = Marca;
			this.Presentacion = Presentacion;
			this.Precio = Precio;
			this.URLFoto = URLFoto;
		}

		public String getIdProductos(){
			return idProductos;
		}
		public void setIdProductos(String idProductos) {
			this.idProductos = idProductos;
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
		public String getDescripcion(){
			return Descripcion;
		}
		public void setDescripcion(){
			this.Descripcion = Descripcion;
		}
		public String getMarca(){
			return Marca;
		}
		public void setMarca(){
			this.Marca = Marca;
		}
		public String getPresentacion(){
			return Presentacion;
		}
		public void setPresentacion(){
			this.Presentacion = Presentacion;
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
	}