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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class actNuevoProducto extends AppCompatActivity {

    BD miBD;
    TextView TempVal;
    Intent TomarFotoIntent;
    Button RegistrarProdct, atras;
    ImageView imgProducto;
    String URL, idProducto, accion = "nuevo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_nuevo_producto);

        miBD = new BD(getApplicationContext(),"",null, 1);

        atras = (Button) findViewById(R.id.btnRegresar);
        atras.setOnClickListener(v -> {
            Atras();
        });

        imgProducto = findViewById(R.id.imgFoto);
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

        RegistrarProdct = (Button) findViewById(R.id.btnRegistrar);
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

                String[] datos = {idProducto, Codigo, Descripcion, Marca, Presentacion, Precio, URL};
                miBD.administracionProductos(accion,datos);

                mostrarMsgToast("Registro guardado con exito");

            } catch (Exception e){mostrarMsgToast(e.getMessage());}
            Atras();
        });
        mostrarDatosProductos();
    }

    public void mostrarDatosProductos(){
        try {
            Bundle Recibirparametros = getIntent().getExtras();
            accion = Recibirparametros.getString("accion");

            if(accion.equals("modificar")){
                String[] datos = Recibirparametros.getStringArray("datos");
                idProducto = datos[0];

                TempVal = findViewById(R.id.txtCodigo);
                TempVal.setText(datos[1]);

                TempVal = findViewById(R.id.txtDescripcion);
                TempVal.setText(datos[2]);

                TempVal = findViewById(R.id.txtMarca);
                TempVal.setText(datos[3]);

                TempVal = findViewById(R.id.txtPresentacion);
                TempVal.setText(datos[4]);

                TempVal = findViewById(R.id.txtPrecio);
                TempVal.setText(datos[5]);

                URL = datos[6];
                Bitmap bitmap = BitmapFactory.decodeFile(URL);
                imgProducto.setImageBitmap(bitmap);
            }
        } catch (Exception e){mostrarMsgToast(e.getMessage());}
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