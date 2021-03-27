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
    ArrayList<productos> datosProductosArrayList;
    LayoutInflater layoutInflater;
    productos misProductos;
    
    public adaptadorImagenes(Context context, ArrayList<productos> datosProductosArrayList){
        this.context = context;
        this.datosProductosArrayList = datosProductosArrayList;
    }

    @Override
    public int getCount() {
        return datosProductosArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datosProductosArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datosProductosArrayList.get(position).getIdProductos());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView tempval = itemView.findViewById(R.id.lblTitulo);
        ImageView imgViewView = itemView.findViewById(R.id.imgPhoto);
        try{
            misProductos = datosProductosArrayList.get(position);
            tempval.setText(misProductos.getDescripcion());

            tempval = itemView.findViewById(R.id.lblMarca);
            tempval.setText(misProductos.getMarca());

            tempval = itemView.findViewById(R.id.lblPresentacion);
            tempval.setText(misProductos.getPresentacion());

            tempval = itemView.findViewById(R.id.lblPrecio);
            tempval.setText(misProductos.getPrecio());

            Bitmap imagenBitMap = BitmapFactory.decodeFile(misProductos.getURLFoto());
            imgViewView.setImageBitmap(imagenBitMap);
        } catch (Exception e){}
        
        return itemView;
    }
}
