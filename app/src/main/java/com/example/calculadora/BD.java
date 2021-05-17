package com.example.calculadora;
//Mauricio Enrique Vásquez Ramirez	USIS007620
//Michelle Brisette Perez Caballero USIS006620
//Elias Mauricio Parada Lozano		USIS030320
//Lisseth Alexandra Gomez Venegas	USIS005620

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {
    public Context micontext;
    static String NombreBD = "db_PicShared";
    static String tblUsuarios = "CREATE TABLE tblUsuarios(idUsuario integer primary key autoincrement, Nombre text, Correo text, Usuario text, Password text)";
    static String tblPublicaciones = "CREATE TABLE tblPublicaciones(idPublicacion integer primary key autoincrement, Usuario text, Descripcion text, Categoria text, Foto text)";
    static String tblComenatarios = "CREATE TABLE tblComentarios()";

    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NombreBD, factory, version);
        micontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblUsuarios);
        db.execSQL(tblPublicaciones);
        //db.execSQL(tblComenatarios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor AministrarUsuarios(String accion, String[] datosUser){
        Cursor datosUsuarios = null;

        try {
            SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
            SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

            switch (accion){
                case "nuevo":
                    sqLiteDatabaseW.execSQL("INSERT INTO tblUsuarios(Nombre, Correo, Usuario, Password) VALUES ('"+ datosUser[1] +"','"+ datosUser[2] +"','"+ datosUser[3] +"','"+ datosUser[4] +"')");
                    break;
                case "seleccionar":
                    datosUsuarios = sqLiteDatabaseR.rawQuery("SELECT Usuario FROM tblUsuarios WHERE Correo='"+ datosUser[1] +"', Password='"+ datosUser[2] +"'", null);
                    break;
                case "seleccionarUser":
                    datosUsuarios = sqLiteDatabaseR.rawQuery("SELECT Usuario, Correo FROM tblUsuarios WHERE Usuario='" + datosUser[3] + "', Correo='"+ datosUser[2] +"'", null);
                    break;
                case "modificar":
                    sqLiteDatabaseW.execSQL("UPDATE tblUsuarios SET Nombre='" + datosUser[1] + "', Correo='" + datosUser[2] + "', Usuario='" + datosUser[3] + "', Password= '" + datosUser[4] + "' WHERE idUsuario='" + datosUser[0] + "'");
                    break;
                case "eliminar":
                    sqLiteDatabaseW.execSQL("DELETE FROM tblUsuarios WHERE idUsuario='" + datosUser[0] + "'");
                    break;
            }
            return datosUsuarios;
        } catch (Exception e){
            Toast.makeText(micontext, "Error en Bd admin User: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return datosUsuarios;
    }

    public Cursor AministrarPublicaciones(String accion, String[] datosPost){
        Cursor datosPublicaciones = null;

        try {
            SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
            SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

            switch (accion){
                case "nuevo":
                    sqLiteDatabaseW.execSQL("INSERT INTO tblPublicaciones(Usuario, Descripcion, Categoria, Foto) VALUES('"+ datosPost[1] +"','"+ datosPost[2] +"','"+ datosPost[3] +"','"+ datosPost[4] +"')");
                    break;
                case "seleccionar":
                    datosPublicaciones = sqLiteDatabaseR.rawQuery("SELECT * FROM tblPublicaciones ORDER BY Descripcion", null);
                    break;
                case "modificar":
                    sqLiteDatabaseW.execSQL("UPDATE tblPublicaciones SET Usuario='" + datosPost[1] + "', Descripcion='" + datosPost[2] + "', Categoria ='" + datosPost[3] + "', Foto= '" + datosPost[4] + "' WHERE idPublicacion='" + datosPost[0] + "'");
                    break;
                case "eliminar":
                    sqLiteDatabaseW.execSQL("DELETE FROM tblPublicaciones WHERE idPublicacion='" + datosPost[0] + "'");
                    break;
            }
            return datosPublicaciones;
        } catch (Exception e){
            Toast.makeText(micontext, "Error en Bd admin Post: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return datosPublicaciones;
    }

    public Cursor AministrarComentarios(String accion, String[] datosComent){
        Cursor datosComentarios = null;

        try {
            SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
            SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

            switch (accion){
                case "nuevo":
                    sqLiteDatabaseW.execSQL("");
                    break;
                case "seleccionar":
                    sqLiteDatabaseR.execSQL("");
                    break;
                case "modificar":
                    sqLiteDatabaseW.execSQL("");
                    break;
                case "eliminar":
                    sqLiteDatabaseW.execSQL("");
                    break;
            }
            return datosComentarios;
        } catch (Exception e){
            Toast.makeText(micontext, "Error en Bd admin User: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return datosComentarios;
    }
}
