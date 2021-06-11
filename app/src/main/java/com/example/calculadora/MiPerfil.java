package com.example.calculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

public class MiPerfil extends AppCompatActivity {
    TextView Usuario;
    Button EditarPerfil, CerrarSesion, NuevoPost;
    GridView MisPublicaciones;
    String accion = "nuevo", UsuarioS;
    int position = 0;
    ArrayList<publicaciones> FotosArrayList = new ArrayList<publicaciones>();
    publicaciones post;
    JSONArray datosJSONArray = new JSONArray();
    JSONObject datosJSONObject;
    DatabaseReference databaseReference;
    Cursor datosPeliculasCursor;

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

        Usuario.setText("Mauricionk");

        //Eventos
        NuevoPost.setOnClickListener(v -> {
            IrEditorPost();
        });

        CargarMisPost();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
      super.onCreateContextMenu(menu, v, menuInfo);
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.menu_mi_perfil, menu);

      try {
          AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
          datosPeliculasCursor.moveToPosition(adapterContextMenuInfo.position);
          position = adapterContextMenuInfo.position;
          //menu.setHeaderTitle(datosJSONArray.getJSONObject(position).getJSONObject("User").getString("usuario"));

      } catch (Exception e){Mensaje("Context Menu: " + e.getMessage());}
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){

        try {
            switch (item.getItemId()){
                case R.id.mnxActualizar:
                    //ActualizarrPeliculas("nuevo");
                    break;
                case R.id.mnxModificar:
                    //ModificarPeli("modificar");
                    break;
                case R.id.mnxEliminar:
                    //eliminarPelicula();
                    break;
            }
        } catch (Exception e){Mensaje("Context Intem: " + e.getMessage());}
        return super.onContextItemSelected(item);
    }

    public void CargarMisPost(){
        UsuarioS = Usuario.getText().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Publicaciones");
        databaseReference.orderByChild("usuario").equalTo(UsuarioS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        post = dataSnapshot.getValue(publicaciones.class);
                        FotosArrayList.add(post);
                        //JSONObject jsonValueobject = new JSONObject();

                        datosJSONObject = new JSONObject();
                        datosJSONObject.put("Categoria", post.getCategoria());
                        datosJSONObject.put("Descripccion", post.getDescripcion());
                        datosJSONObject.put("key", post.getIdPublicacion());
                        datosJSONObject.put("URL", post.getURLFoto());
                        datosJSONObject.put("URLOnline", post.getURLFotoFirebase());
                        datosJSONObject.put("User", post.getUsuario());
                        datosJSONArray.put(datosJSONObject);
                    }
                    adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), FotosArrayList);
                    MisPublicaciones.setAdapter(adaptadorImagenes);
                    registerForContextMenu(MisPublicaciones);

                } catch (Exception e){Mensaje("Mostrar posts: " + e.getMessage());}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void IrEditorPost(){
        Intent Editor = new Intent(this, NuevoPost.class);
        //Editor.putExtra("accion", accion);
        startActivity(Editor);
    }

    private void Mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}