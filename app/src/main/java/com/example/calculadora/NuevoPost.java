package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class NuevoPost extends AppCompatActivity {
    //Mauricio Enrique Vásquez Ramirez	USIS007620
    //Michelle Brisette Perez Caballero USIS006620
    //Elias Mauricio Parada Lozano		USIS030320
    //Lisseth Alexandra Gomez Venegas	USIS005620

    ImageView Imagen;
    EditText txtDescripcion, txtCategoria, U;
    Button Publicar, Atras;
    String accion = "nuevo", URL,URLFireStore, Usuario, miToken, key;
    String Descripcion, Categoria;
    private static final int RPQ= 100;
    private static final int RIG= 101;
    //private static final int RVD= 102;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_post);

        //Relacion xml-java
        Imagen = (ImageView) findViewById(R.id.imgFoto);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcionEditor);
        txtCategoria = (EditText) findViewById(R.id.txtCategoriaPost);
        Publicar = (Button)findViewById(R.id.btnPublicar);
        Atras = (Button) findViewById(R.id.btnAtrasEditor);
        U = (EditText) findViewById(R.id.txtUsurioLogin);
        /*if (U.getText() != null){
            Usuario = U.getText().toString();
        }*/

        Usuario = "Mauricionk";

        //Eventos
        Atras.setOnClickListener(v -> {
            irMiPerfil();
        });

        Imagen.setOnClickListener(v -> {
            SeleccionarImagen();
        });

        Publicar.setOnClickListener(v -> {
            SubirFoto();
        });
        Permisos();
    }

    void SubirFoto(){
        Mensaje("Subiendo...");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(URL));
        final StorageReference reference = storageReference.child("Fotos/"+file.getLastPathSegment());

        final UploadTask uploadTask = reference.putFile(file);
        uploadTask.addOnFailureListener(e -> {
            Mensaje("Fallo al subir foto al servidor: " + e.getMessage());
        });
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Mensaje("Foto en la nube");
            Task<Uri> descargaUri = uploadTask.continueWithTask(task -> reference.getDownloadUrl()).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    URLFireStore = task.getResult().toString();
                    SubirPost();
                } else {
                    Mensaje("La foto se subio pero no se pudo vincular al post.");
                }
            });
        });
    }

    private void SubirPost(){
        try {
            Descripcion = txtDescripcion.getText().toString();
            Categoria = txtCategoria.getText().toString();

            databaseReference = FirebaseDatabase.getInstance().getReference("Publicaciones");
            key = databaseReference.push().getKey();
            if (miToken == "" || miToken == null) {
                obtenerToken();
            }
            if( miToken!=null || key == null){
                publicaciones post = new publicaciones(key, Usuario, Descripcion, Categoria, URL, URLFireStore, miToken);

                if (key == null){
                    databaseReference.child(key).setValue(post).addOnSuccessListener(aVoid -> {
                       Mensaje("Imagen Publicada");
                       irMiPerfil();
                    });
                } else {Mensaje("NO se publico la imagen en la base de datos de firebase");}
            } else {Mensaje("NO pude obtener el identificar de tu telefono, por favor intentalo mas tarde.");}


        } catch (Exception e){Mensaje("Error subir post: " + e.getMessage());}
    }

    private void SeleccionarImagen(){
        Intent elegir =  new Intent(Intent.ACTION_GET_CONTENT );
        elegir.setType("image/*");
        startActivityForResult(elegir, RIG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (requestCode == RIG) {
                    Uri foto = data.getData();
                    Imagen.setImageURI(foto);

                    URL = getRealURL(this, foto);
                    Mensaje(URL);
                }
            }
        } catch (Exception e){Mensaje("ActResult: " + e.getMessage());}
    }

    public static String getRealURL(final Context context, final Uri uri) {
        final boolean isPie = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
        if (isPie && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String Id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(Id));
                return getDataColum(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

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


    private void Permisos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(NuevoPost.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            } else {
                ActivityCompat.requestPermissions(NuevoPost.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RPQ);
            }
        } else {

        }
    }

    private void obtenerToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if( !task.isSuccessful() ){
                return;
            }
            miToken = task.getResult();
        });
    }

    public void irMiPerfil(){
        Intent Miperfil = new Intent(this, MiPerfil.class);
        startActivity(Miperfil);
    }
    private void Mensaje(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == RPQ){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            } else {Mensaje("Conseda los permisos para proseguir");}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}