
package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.AbstractCursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
	FloatingActionButton btnRegistrar;
	BD miBD;
	ListView lstBuscar;
	Cursor datosProductosCursor = null;
	ArrayList<productos> productosArrayList = new ArrayList<productos>();
	ArrayList<productos> productosArrayListCopy=new ArrayList<productos>();
	productos misproductos; //esto lo veo luego
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRegistrar = findViewById(R.id.btnRegistProducto);
		btnRegistrar.setOnClickListener(v -> {
			RegistrarProduct("nuevo", new String[]{});
		});

		try {
			obteberDatosProductos();
			buscarProducto();
		} catch (Exception e){mostrarMsgToask(e.getMessage());}

	}

	@Override //Se contruyé el menú.
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_productos, menu);

		AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
		datosProductosCursor.moveToPosition(adapterContextMenuInfo.position);
		menu.setHeaderTitle(datosProductosCursor.getString(1));
	}

	@Override //En esta sección se declararán las funciones del menu.
	public boolean onContextItemSelected(@NonNull MenuItem item) {
		try {
			switch (item.getItemId()) {
				case R.id.mnxAgregar:
					RegistrarProduct("nuevo", new String[]{});
					break;
				case R.id.mnxModificar:
					String[] datos = {
							datosProductosCursor.getString(0),//ID_Producto
							datosProductosCursor.getString(1),//Codigo_Producto
							datosProductosCursor.getString(2),//Descripcion_Producto
							datosProductosCursor.getString(3),//Marca_Producto
							datosProductosCursor.getString(4), //Presentacion_Producto
							datosProductosCursor.getString(5), //Precio_Produto
							datosProductosCursor.getString(6) //URL_Image
					};
					RegistrarProduct("modificar", datos);
					break;
				case R.id.mnxEliminar:
					eliminarProducto();
					break;
			}
		} catch (Exception ex) {
			mostrarMsgToask(ex.getMessage());
		}
		return super.onContextItemSelected(item);
	}

	//Abrir pestaña de registro de productos
	private void RegistrarProduct(String accion, String[] datos) {
		try {
			Bundle parametrosProducts = new Bundle();
			parametrosProducts.putString("accion", accion);
			parametrosProducts.putStringArray("datos", datos);

			Intent agregarAmigos = new Intent(getApplicationContext(), actNuevoProducto.class);
			agregarAmigos.putExtras(parametrosProducts);
			startActivity(agregarAmigos);
		} catch (Exception e) {
			mostrarMsgToask(e.getMessage());
		}
	}

	//Eliminar un producto.
	private void eliminarProducto() {
		try {
			AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
			confirmacion.setTitle("¿Desea eliminar el siguiente producto?");
			confirmacion.setMessage(datosProductosCursor.getString(1));
			confirmacion.setPositiveButton("Si", (dialog, which) -> {
				miBD = new BD(getApplicationContext(), "", null, 1);
				datosProductosCursor = miBD.administracionProductos("eliminar", new String[]{datosProductosCursor.getString(0)});//idAmigo
				obteberDatosProductos();
				mostrarMsgToask("¡Registro Eliminado con exito!");
				dialog.dismiss();//cerrar el cuadro de dialogo
			});
			confirmacion.setNegativeButton("No", (dialog, which) -> {
				mostrarMsgToask("Eliminacion cancelada por el usuario...");
				dialog.dismiss();
			});
			confirmacion.create().show();
		} catch (Exception ex) {
			mostrarMsgToask(ex.getMessage());
		}
	}

	//Obtener los datos de la BD, más no mostrarlos.
	private void obteberDatosProductos() {
		miBD = new BD(getApplicationContext(), "", null, 1);
		datosProductosCursor = miBD.administracionProductos("seleccionar", null);
		if (datosProductosCursor.moveToFirst() ) {//si hay datos que mostrar
			mostrarDatosProductos();
		} else {//sino que llame para agregar nuevos amigos...
			mostrarMsgToask("No hay datos de amigos que mostrar, por favor agregue nuevos amigos...");
			RegistrarProduct("nuevo", new String[]{});
		}
	}

	//Mostrar datros de la BD
	private void mostrarDatosProductos() {
		lstBuscar = findViewById(R.id.ltsBuscar);
		productosArrayList.clear();
		productosArrayListCopy.clear();
		do {
			misproductos = new productos(
					datosProductosCursor.getString(0),//ID_Producto
					datosProductosCursor.getString(1),//Codigo_Producto
					datosProductosCursor.getString(2),//Descripcion_Producto
					datosProductosCursor.getString(3),//Marca_Producto
					datosProductosCursor.getString(4), //Presentacion_Producto
					datosProductosCursor.getString(5), //Precio_Produto
					datosProductosCursor.getString(6) //URL_Image
			);
			productosArrayList.add(misproductos);
		} while (datosProductosCursor.moveToNext());
		adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), productosArrayList);
		lstBuscar.setAdapter(adaptadorImagenes);

		registerForContextMenu(lstBuscar);

		productosArrayListCopy.addAll(productosArrayList);
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
				} catch (Exception e){mostrarMsgToask(e.getMessage());}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	//Mostrar un mensaje
	private void mostrarMsgToask(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
}


	class productos {
		String idProductos;
		String Codigo;
		String Descripcion;
		String Marca;
		String Presentacion;
		String Precio;
		String URLFoto;


		public productos(String idProductos, String Codigo, String Descripcion, String Marca, String Presentacion, String Precio, String URLFoto) {
			this.idProductos = idProductos;
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