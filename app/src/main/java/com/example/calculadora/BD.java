package com.example.calculadora;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {
    static String nombreBD = "db_Productos";
    static String tblProductos = "CREATE TABLE tblProductos(idProductos integer primary key autoincrement, Codigo text, Descripcion text, Marca text, Presentacion text, Precio text, URLFoto text)";

    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombreBD, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblProductos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor administracionProductos(String accion, String[] datos){
        Cursor datosCursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "nuevo":
                sqLiteDatabaseW.execSQL("INSERT INTO tblProductos(Codigo, Descripcion, Marca, Presentacion, Precio, URLFoto) VALUES ('"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"','"+datos[5]+",'"+datos[6]+"')");
                break;
            case "seleccionar":
                sqLiteDatabaseR.execSQL("SELECT * FROM tblProductos ORDER BY Descripcion", null);
                break;
            case "modificar":
                sqLiteDatabaseW.execSQL("UPDATE tblProductos SET Codigo='"+datos[1]+"', Descripcion='"+datos[2]+"', Marca='"+datos[3]+"', Presentacion='"+datos[4]+"', Precio='"+datos[5]+"', URLFoto='"+datos[6]+"' WHERE idProductos='"+datos[0]+"'");
                break;
            case "eliminar":
                sqLiteDatabaseW.execSQL("DELETE * FROM tblProductos WHERE idProductos='"+datos[0]+"'");
        }
        return datosCursor;
    }
}
