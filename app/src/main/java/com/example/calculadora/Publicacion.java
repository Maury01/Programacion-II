package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;
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
    String URL, URLFireBase, key, DescripcionText, CategoriaText, Usuario, Comentario, idComentario;
    publicaciones post;
    Context context;
    DatabaseReference databaseReference;



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
        RecibirPost();
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

    }
    public void EnviarComentario(){
        Comentario = EscribirComentario.getText().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Comentarios");
        idComentario = databaseReference.push().getKey();

        //Hacer clase comentarios


    }

    void mostrarFoto(){
        Glide.with(getApplicationContext()).load(URLFireBase).into(ImagenPost);
    }

    public void PaginaPrincipal(){
        Intent PagPrincipal = new Intent(this, MainActivity.class);
        startActivity(PagPrincipal);
    }
    private void Mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}