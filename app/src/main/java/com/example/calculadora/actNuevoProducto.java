package com.example.calculadora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class actNuevoProducto extends AppCompatActivity {

    BD miBD;
    TextView TempVal;
    Intent TomarFotoIntent;
    Button RegistrarProdct, atras;
    ImageView imgProducto;
    String URL, idProducto, rev, accion = "nuevo";
    utilidades miUrl;
    DetectarInternet di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_nuevo_producto);

        miBD = new BD(getApplicationContext(),"",null, 1);
        imgProducto = findViewById(R.id.imgFoto);
        atras = (Button) findViewById(R.id.btnRegresar);
        RegistrarProdct = (Button) findViewById(R.id.btnRegistrar);

        atras.setOnClickListener(v -> {
            Atras();
        });


        imgProducto.setOnClickListener(v -> {
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

        RegistrarProdct.setOnClickListener(v -> {
            try {
                TempVal = (TextView) findViewById(R.id.txtCodigo);
                String Codigo = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtDescripcion);
                String Descripcion = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtMarca);
                String Marca = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtPresentacion);
                String Presentacion = TempVal.getText().toString();

                TempVal = (TextView) findViewById(R.id.txtPrecio);
                String Precio = TempVal.getText().toString();

                JSONObject datosProduct =new JSONObject();
                if (accion.equals("modificar") && idProducto.length() > 0 && rev.length() > 0){
                    datosProduct.put("_id", idProducto);
                    datosProduct.put("_rev", rev);
                }
                datosProduct.put("Codigo",Codigo);
                datosProduct.put("Descripcion", Descripcion);
                datosProduct.put("Marca", Marca);
                datosProduct.put("Presentacion", Presentacion);
                datosProduct.put("Precio", Precio);
                datosProduct.put("URLFoto", URL);

                String[] datos = {idProducto, Codigo, Descripcion, Marca, Presentacion, Precio, URL};

                di = new DetectarInternet(getApplicationContext());
                if (di.hayConexion()){
                    EnviarDatosProductos objGuardarProduc = new EnviarDatosProductos(getApplicationContext());
                    String resp = objGuardarProduc.execute(datosProduct.toString()).get();
                }

                miBD.administracionProductos(accion,datos);
                mostrarMsgToast("Registro guardado con exito");
                Atras();
            } catch (Exception e){mostrarMsgToast("boton enviar: "+e.getMessage());}

        });
        mostrarDatosProductos();
    }

    public void mostrarDatosProductos(){
        try {
            Bundle Recibirparametros = getIntent().getExtras();
            accion = Recibirparametros.getString("accion");

            if(accion.equals("modificar")){
                JSONObject datos = new JSONObject(Recibirparametros.getString("datos")).getJSONObject("value");
                idProducto = datos.getString("_id");
                rev = datos.getString("_rev");

                TempVal = findViewById(R.id.txtCodigo);
                TempVal.setText(datos.getString("Codigo"));

                TempVal = findViewById(R.id.txtDescripcion);
                TempVal.setText(datos.getString("Descripcion"));

                TempVal = findViewById(R.id.txtMarca);
                TempVal.setText(datos.getString("Marca"));

                TempVal = findViewById(R.id.txtPresentacion);
                TempVal.setText(datos.getString("Presentacion"));

                TempVal = findViewById(R.id.txtPrecio);
                TempVal.setText(datos.getString("Precio"));

                URL = datos.getString("URLFoto");
                Bitmap bitmap = BitmapFactory.decodeFile(URL);
                imgProducto.setImageBitmap(bitmap);
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
                imgProducto.setImageBitmap(imagenBitmap);
                mostrarMsgToast(URL.toString());
            }
            if(requestCode == 2 && resultCode == RESULT_OK){
                Uri path = data.getData();
                imgProducto.setImageURI(path);
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

    public void Atras (){
        Intent Regresar = new Intent(this, MainActivity.class);
        startActivity(Regresar);
    }

    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}