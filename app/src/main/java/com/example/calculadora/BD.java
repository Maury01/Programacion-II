package com.example.calculadora;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {
    Context micontext;
    static String nombreBD = "db_Peliculas";
    static String tblPeliculas = "CREATE TABLE tblPeliculas(idPelicula integer primary key autoincrement, Codigo text, Titulo text, Sinopsis text, Duracion text, Precio text, URLFoto text, URLTrailer text)";

    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombreBD, factory, version);
        micontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblPeliculas);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor administracionPeliculas(String accion, String[] datos){
        try {
            Cursor datosCursor = null;
            SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
            SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

            switch (accion){
                case "nuevo":
                    sqLiteDatabaseW.execSQL("INSERT INTO tblPeliculas(Codigo, Titulo, Sinopsis, Duracion, Precio, URLFoto, URLTrailer) VALUES ('"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"','"+datos[5]+"','"+datos[6]+"','"+datos[7]+"')");
                    break;
                case "seleccionar":
                    datosCursor = sqLiteDatabaseR.rawQuery("SELECT * FROM tblPeliculas ORDER BY Titulo", null);
                    break;
                case "modificar":
                    sqLiteDatabaseW.execSQL("UPDATE tblPeliculas SET Codigo='"+datos[1]+"', Titulo='"+datos[2]+"', Sinopsis='"+datos[3]+"', Duracion='"+datos[4]+"', Precio='"+datos[5]+"', URLFoto='"+datos[6]+"', URLTrailer='"+datos[7]+"' WHERE idPelicula='"+datos[0]+"'");
                    break;
                case "eliminar":
                    sqLiteDatabaseW.execSQL("DELETE FROM tblPeliculas WHERE idPelicula='"+datos[0]+"'");
            }
            return datosCursor;
        } catch (Exception e){
            Toast.makeText(micontext, "Error en admin. BD: "+ e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
