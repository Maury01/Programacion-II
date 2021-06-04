package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

public class MiPerfil extends AppCompatActivity {
    TextView Usuario;
    Button EditarPerfil, CerrarSesion, NuevoPost;
    GridView MisPublicaciones;
    String accion = "nuevo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        //Relacion con xml
        Usuario = (TextView) findViewById(R.id.lblUsuarioPerfil);
        EditarPerfil = (Button) findViewById(R.id.btnEditPerfil);
        CerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);
        NuevoPost = (Button) findViewById(R.id.btnNuevaPublicacion);
        MisPublicaciones = (GridView) findViewById(R.id.grvMisImagenes);

        //Eventos
        NuevoPost.setOnClickListener(v -> {
            IrEditorPost();
        });

    }

    public void IrEditorPost(){
        Intent Editor = new Intent(this, NuevoPost.class);
        //Editor.putExtra("accion", accion);
        startActivity(Editor);
    }
}