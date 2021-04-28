package com.example.calculadora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class actNuevoProducto extends AppCompatActivity {

    BD miBD;
    TextView TempVal, DireccionVideo;
    Intent TomarFotoIntent;
    Button RegistrarPeli, ElegirTrailer, atras;
    ImageView imgPelicula;
    String URL, URLTrailer, idPelicula, rev, accion = "nuevo";
    utilidades miUrl;
    DetectarInternet di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_nuevo_producto);

        miBD = new BD(getApplicationContext(),"",null, 1);
        imgPelicula = findViewById(R.id.imgFoto);
        atras = (Button) findViewById(R.id.btnRegresar);
        RegistrarPeli = (Button) findViewById(R.id.btnRegistrar);
        ElegirTrailer = (Button) findViewById(R.id.btnVideo);
        DireccionVideo = (TextView) findViewById(R.id.lblVideo);

        atras.setOnClickListener(v -> {
            Atras();
        });



        imgPelicula.setOnClickListener(v -> {
            final CharSequence[] opciones = {"Tomar Foto", "Abrir de Galeria", "Cancelar"};
            final AlertDialog.Builder alerta = new AlertDialog.Builder(actNuevoProducto.this);
            alerta.setTitle("Selecciona una Opción");
            alerta.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Tomar Foto")){
                        tomarFotoProducto();
                    } else {
                        dialog.dismiss();
                    }
                    if (opciones[which].equals("Abrir de Galeria")){
                        ElegirDeGaleria();
                    }
                }
            });
            alerta.show();

        });

        RegistrarPeli.setOnClickListener(v -> {
            try {
                TempVal = (TextView) findViewById(R.id.txtCodigo);
                String Codigo = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtTitulo);
                String Titulo = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtSinopsis);
                String Sinopsis = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtDuracion);
                String Duracion = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtPrecio);
                String Precio = TempVal.getText().toString();

                JSONObject datosPeli =new JSONObject();
                if (accion.equals("modificar") && idPelicula.length() > 0 && rev.length() > 0){
                    datosPeli.put("_id", idPelicula);
                    datosPeli.put("_rev", rev);
                }
                datosPeli.put("Codigo",Codigo);
                datosPeli.put("Descripcion", Titulo);
                datosPeli.put("Marca", Sinopsis);
                datosPeli.put("Presentacion", Duracion);
                datosPeli.put("Precio", Precio);
                datosPeli.put("URLFoto", URL);
                datosPeli.put("URLTrailer", URLTrailer);

                String[] datos = {idPelicula, Codigo, Titulo, Sinopsis, Duracion, Precio, URL, URLTrailer};

                di = new DetectarInternet(getApplicationContext());
                if (di.hayConexion()){
                    EnviarDatosPelicula objGuardarPeli = new EnviarDatosPelicula(getApplicationContext());
                    String resp = objGuardarPeli.execute(datosPeli.toString()).get();
                }

                miBD.administracionPeliculas(accion,datos);
                mostrarMsgToast("Registro guardado con exito");
                Atras();
            } catch (Exception e){mostrarMsgToast("boton enviar: "+e.getMessage());}

        });
        mostrarDatosPeliculas();
    }

    public void mostrarDatosPeliculas(){
        try {
            Bundle Recibirparametros = getIntent().getExtras();
            accion = Recibirparametros.getString("accion");

            if(accion.equals("modificar")){
                JSONObject datos = new JSONObject(Recibirparametros.getString("datos")).getJSONObject("value");
                idPelicula = datos.getString("_id");
                rev = datos.getString("_rev");

                TempVal = findViewById(R.id.txtCodigo);
                TempVal.setText(datos.getString("Codigo"));

                TempVal = findViewById(R.id.txtTitulo);
                TempVal.setText(datos.getString("Titulo"));

                TempVal = findViewById(R.id.txtSinopsis);
                TempVal.setText(datos.getString("Snopsis"));

                TempVal = findViewById(R.id.txtDuracion);
                TempVal.setText(datos.getString("Duracion"));

                TempVal = findViewById(R.id.txtPrecio);
                TempVal.setText(datos.getString("Precio"));

                URL = datos.getString("URLFoto");
                Bitmap bitmap = BitmapFactory.decodeFile(URL);
                imgPelicula.setImageBitmap(bitmap);

                URLTrailer = datos.getString("URLTrailer");
                DireccionVideo.setText(URLTrailer);

            }
        } catch (Exception e){mostrarMsgToast("act mostrar datos"+e.getMessage());}
    }

    private void tomarFotoProducto(){
        TomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (TomarFotoIntent.resolveActivity(getPackageManager())!= null){
            File PhotoProductos = null;
            try {
                PhotoProductos =CrearImagenProducto();
            } catch (Exception e) {mostrarMsgToast(e.getMessage());}
            if (PhotoProductos != null){
                try {
                    Uri uriPhotoPtoductos = FileProvider.getUriForFile(actNuevoProducto.this, "com.example.calculadora.fileprovider", PhotoProductos);
                    TomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhotoPtoductos);
                    startActivityForResult(TomarFotoIntent, 1);
                } catch (Exception e) {mostrarMsgToast(e.getMessage());}
            } else {mostrarMsgToast("No fue posible tomar la foto");}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK){
                Bitmap imagenBitmap = BitmapFactory.decodeFile(URL);
                imgPelicula.setImageBitmap(imagenBitmap);
                mostrarMsgToast(URL.toString());
            }
            if(requestCode == 2 && resultCode == RESULT_OK){
                Uri path = data.getData();
                imgPelicula.setImageURI(path);
                //URL = path.toString();
                mostrarMsgToast(path.toString());
            }
        } catch (Exception e){mostrarMsgToast(e.getMessage());}
    }

    private File CrearImagenProducto() throws Exception{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreImagen = "imagen_" + timeStamp + "_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (dirAlmacenamiento.exists() == false){
            dirAlmacenamiento.mkdirs();
        }
        File image = File.createTempFile(nombreImagen, ".jpg", dirAlmacenamiento);
        URL = image.getAbsolutePath();
        return image;
    }

    private void ElegirDeGaleria(){
        Intent elegir = new Intent(Intent.ACTION_GET_CONTENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        elegir.setType("image/*");
        startActivityForResult(getIntent().createChooser(elegir, "seleccione la aplicacion"), 2);
    }

    private void VerTrailer(){

    }

    public void Atras (){
        Intent Regresar = new Intent(this, MainActivity.class);
        startActivity(Regresar);
    }

    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}