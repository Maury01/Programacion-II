package com.example.calculadora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<publicaciones> ListaDePost;
    LayoutInflater layoutInflater;
    publicaciones Post;

    public adaptadorImagenes(Context context, ArrayList<publicaciones> listaDePost) {
        this.context = context;
        ListaDePost = listaDePost;
    }

    @Override
    public int getCount() {
        return ListaDePost.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaDePost.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = layoutInflater.inflate(R.layout.adaptador_de_imagenes, parent, false);
        ImageView imgView = itemView.findViewById(R.id.imgPhoto);
        TextView tempvalue = itemView.findViewById(R.id.lblTitulo);
        try {
            Post = ListaDePost.get(position);
            tempvalue.setText(Post.getUsuario());
            //Glide.with(context).load(Post.getUrlPhotoFirestore()).into(imgView);
        } catch (Exception e){
            tempvalue.setText(e.getMessage());
        }
        return itemView;
    }
}
