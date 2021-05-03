package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class actNuevoProducto extends AppCompatActivity {
    //Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

    BD miBD;
    TextView TempVal, DireccionVideo, imagen;
    Intent TomarFotoIntent;
    Button RegistrarPeli, ElegirTrailer, atras, Vertrailer;
    ImageView imgPelicula;
    VideoView vidPelicula;
    String URL, URLTrailer, idPelicula, rev, accion = "nuevo";
    utilidades miUrl;
    DetectarInternet di;
    private static final int RPQ= 100;
    private static final int RIG= 101;
    private static final int RVD= 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_nuevo_producto);

        miBD = new BD(getApplicationContext(),"",null, 1);
        imgPelicula = findViewById(R.id.imgFoto);
        atras = (Button) findViewById(R.id.btnRegresar);
        RegistrarPeli = (Button) findViewById(R.id.btnRegistrar);
        ElegirTrailer = (Button) findViewById(R.id.btnVideo);
        DireccionVideo = (TextView) findViewById(R.id.lblURLVideo);
        vidPelicula = (VideoView) findViewById(R.id.vdoVideo);
        imagen = (TextView) findViewById(R.id.lblURLImagen);
        Vertrailer = (Button) findViewById(R.id.VerTrailer);

        atras.setOnClickListener(v -> { Atras(); });

        imgPelicula.setOnClickListener(v -> { ElegirImagen();});

        ElegirTrailer.setOnClickListener(v -> {ElegirVideo();});

        Vertrailer.setOnClickListener(v -> { VerTrailer(); });



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
                datosPeli.put("Titulo", Titulo);
                datosPeli.put("Sinopsis", Sinopsis);
                datosPeli.put("Duracion", Duracion);
                datosPeli.put("Precio", Precio);
                datosPeli.put("URLFoto", URL);
                datosPeli.put("URLTrailer", URLTrailer);

                if (URL != ""){
                    datosPeli.put("URLFoto",URL);
                }else {
                    URL = "Empty";
                    datosPeli.put("URLFoto",URL);
                }
                if (URLTrailer != ""){
                    datosPeli.put("URLTrailer",URLTrailer);
                }else {
                    URLTrailer = "Empty";
                    datosPeli.put("URLTrailer",URLTrailer);
                }

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
        Permisos();
        Controles();
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
                TempVal.setText(datos.getString("Sinopsis"));

                TempVal = findViewById(R.id.txtDuracion);
                TempVal.setText(datos.getString("Duracion"));

                TempVal = findViewById(R.id.txtPrecio);
                TempVal.setText(datos.getString("Precio"));

                URL = datos.getString("URLFoto");
                Bitmap bitmap = BitmapFactory.decodeFile(URL);
                imgPelicula.setImageBitmap(bitmap);

                URLTrailer = datos.getString("URLTrailer");
                vidPelicula.setVideoURI(Uri.parse(URLTrailer));
                DireccionVideo.setText(URLTrailer);

            }
        } catch (Exception e){mostrarMsgToast("act mostrar datos"+e.getMessage());}
    }

    private void Permisos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(actNuevoProducto.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            } else {
                ActivityCompat.requestPermissions(actNuevoProducto.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RPQ);
            }
        } else {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK && data != null){
                if (requestCode == RIG){
                    Uri foto =  data.getData();
                    imgPelicula.setImageURI(foto);

                    URL =  getRealURL(this, foto);
                    imagen.setText("imagen: " + URL);
                } else if (requestCode == RVD){
                    Uri video = data.getData();
                    vidPelicula.setVideoURI(video);
                    URLTrailer = getRealURL(this, video);
                    DireccionVideo.setText("Video: " + URLTrailer);

                }
            }

        } catch (Exception e){mostrarMsgToast("Act.Result: "+e.getMessage());}
    }



    private void ElegirImagen(){
        Intent elegir =  new Intent(Intent.ACTION_GET_CONTENT );
        elegir.setType("image/*");
        startActivityForResult(elegir, RIG);
    }

    private void ElegirVideo(){
        Intent elegir = new Intent(Intent.ACTION_GET_CONTENT);
        elegir.setType("video/*");
        startActivityForResult(elegir, RVD);
    }
    private void Controles(){
        MediaController mediaController = new MediaController(this);
        vidPelicula.setMediaController(mediaController);
        mediaController.setAnchorView(vidPelicula);
    }

    public void Atras (){
        Intent Regresar = new Intent(this, MainActivity.class);
        startActivity(Regresar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == RPQ){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            } else {mostrarMsgToast("Conseda los permisos para proseguir");}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
    public void VerTrailer(){
        Intent reproducir = new Intent(getApplicationContext(), Reproductor.class);
        startActivity(reproducir);
    }

    public static String getRealURL(final Context context, final Uri uri){
        final boolean isPie = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
        if (isPie && DocumentsContract.isDocumentUri(context, uri)){
            if (isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory() +  "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)){
                final String Id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(Id));
                return getDataColum(context, contentUri, null, null);
            } else if (isMediaDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;

                if ("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)){
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColum(context, contentUri, selection, selectionArgs);
            }
        }
        return null;
    }

    public static String getDataColum(Context context, Uri uri, String selection, String[] selectionArgs){
        Cursor cursor = null;
        final String colum = "_data";
        final String[] projection = { colum };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()){
                final int colum_index = cursor.getColumnIndexOrThrow(colum);
                return cursor.getString(colum_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}