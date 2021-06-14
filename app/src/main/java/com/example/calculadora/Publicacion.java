package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620
//id firebase picshared-a8ffb

public class Publicacion extends AppCompatActivity {
    Button atras, ok;
    TextView TituloUsuario, Descripcion, Categoria;
    ImageView ImagenPost;
    ListView Comentarios;
    EditText EscribirComentario;
    String URL, URLFireBase, key, DescripcionText, CategoriaText, Usuario, Comentario, idComentario, miToken;
    publicaciones post;
    Context context;
    DatabaseReference databaseReference;
    JSONArray datosJSONArray = new JSONArray();
    JSONObject datosJSONObject;
    ArrayList<comentarios> ComentariosArrayList = new ArrayList<comentarios>();
    Cursor datosPeliculasCursor;
    comentarios coments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        //relacion java-xml
        atras = (Button) findViewById(R.id.btnAtrasPost);
        ok = (Button) findViewById(R.id.btnEnviarComentario);
        TituloUsuario = (TextView) findViewById(R.id.lblTituloPost);
        Descripcion = (TextView) findViewById(R.id.lblDescripcionPost);
        Categoria = (TextView) findViewById(R.id.lblCategoriaOtherPost);
        ImagenPost = (ImageView) findViewById(R.id.imgFotoOtherUser);
        Comentarios = (ListView) findViewById(R.id.ltsComentarios);
        EscribirComentario = (EditText) findViewById(R.id.txtComentar);

        //Eventos
        atras.setOnClickListener(v -> {
            PaginaPrincipal();
        });

        ok.setOnClickListener(v -> {
            EnviarComentario();
        });

        //Funciones
        //try {
        obtenerToken();

        //} catch (Exception e){ Mensaje("Cargar: " + e.getMessage());}
        RecibirPost();
        MostrarComentarios();

    }

    public void RecibirPost(){
        try {
            Bundle parametros = getIntent().getExtras();
            if (parametros.getString("key") != null && parametros.getString("key") != ""){
                key = parametros.getString("key");
                URL = parametros.getString("URL");
                URLFireBase = parametros.getString("URLOnline");
                DescripcionText = parametros.getString("Descripccion");
                CategoriaText = parametros.getString("Categoria");
                Usuario = parametros.getString("User");

                TituloUsuario.setText("Publicacion de @" + Usuario);
                Descripcion.setText(DescripcionText);
                Categoria.setText("Categoria: " + CategoriaText);
                mostrarFoto();

            }

        } catch (Exception e){Mensaje("Recibir Post: " + e.getMessage());}
    }

    public void MostrarComentarios(){
        Mensaje("key: "+key);
        databaseReference = FirebaseDatabase.getInstance().getReference("Comentarios");
        databaseReference.orderByChild("idPublicacion").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        coments = dataSnapshot.getValue(comentarios.class);
                        ComentariosArrayList.add(coments);

                        datosJSONObject = new JSONObject();
                        datosJSONObject.put("IdComentario", coments.getIdComentarios());
                        datosJSONObject.put("idPublicacion", coments.getIdPublicacion());
                        datosJSONObject.put("Texto", coments.getTexto());
                        datosJSONObject.put("Usuario", coments.getUsaurio());
                        datosJSONArray.put(datosJSONObject);
                    }
                    adaptadorComentarios adaptadorComentarios = new adaptadorComentarios(getApplicationContext(), ComentariosArrayList);
                    Comentarios.setAdapter(adaptadorComentarios);

                } catch (Exception e){Mensaje("Mostrar COmentarios: " + e.getMessage());}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void EnviarComentario(){
        try {
            Comentario = EscribirComentario.getText().toString();
            databaseReference = FirebaseDatabase.getInstance().getReference("Comentarios");
            idComentario = databaseReference.push().getKey();

            if (miToken == "" || miToken == null) {
                obtenerToken();
            }
            if( miToken!=null || idComentario == null){
                coments = new comentarios(idComentario, Usuario, Comentario, key);

                if (idComentario != null){
                    databaseReference.child(idComentario).setValue(coments).addOnSuccessListener(aVoid -> {
                        Mensaje("Comentario Publicado");
                        EscribirComentario.setText("");
                    });
                } else {Mensaje("El comentario no se publico");}
            } else {Mensaje("NO pude obtener el identificar de tu telefono, por favor intentalo mas tarde.");}
        } catch (Exception e){Mensaje("Enviar Comentario: " + e.getMessage());}

    }

    void mostrarFoto(){
        Glide.with(getApplicationContext()).load(URLFireBase).into(ImagenPost);
    }

    private void obtenerToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if( !task.isSuccessful() ){
                return;
            }
            miToken = task.getResult();
        });
    }

    public void PaginaPrincipal(){
        Intent PagPrincipal = new Intent(this, MainActivity.class);
        startActivity(PagPrincipal);
    }
    private void Mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}