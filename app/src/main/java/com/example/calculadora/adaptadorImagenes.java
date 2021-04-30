package com.example.calculadora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class adaptadorImagenes extends BaseAdapter {

    Context context;
    ArrayList<peliculas> datosPeliculasArrayList;
    LayoutInflater layoutInflater;
    peliculas misPeliculas;
    
    public adaptadorImagenes(Context context, ArrayList<peliculas> datosPeliculasArrayList){
        this.context = context;
        this.datosPeliculasArrayList = datosPeliculasArrayList;
    }

    @Override
    public int getCount() {

        return datosPeliculasArrayList.size();
    }
    @Override
    public Object getItem(int position) {

        return datosPeliculasArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datosPeliculasArrayList.get(position).getIdPelicula());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView tempval = itemView.findViewById(R.id.lblTitulo);
        ImageView imgViewView = itemView.findViewById(R.id.imgPhoto);
        try{
            misPeliculas = datosPeliculasArrayList.get(position);
            tempval.setText(misPeliculas.getTitulo());

            tempval = itemView.findViewById(R.id.lblDuracion);
            tempval.setText(misPeliculas.getDuracion());

            tempval = itemView.findViewById(R.id.lblCodigo);
            tempval.setText(misPeliculas.getCodigo());

            tempval = itemView.findViewById(R.id.lblPrecio);
            tempval.setText("$"+misPeliculas.getPrecio());

            Bitmap imagenBitMap = BitmapFactory.decodeFile(misPeliculas.getURLFoto());
            imgViewView.setImageBitmap(imagenBitMap);
        } catch (Exception e){}
        return itemView;
    }
}
