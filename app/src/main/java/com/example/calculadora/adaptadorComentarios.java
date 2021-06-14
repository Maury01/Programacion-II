package com.example.calculadora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorComentarios extends BaseAdapter {
    Context context;
    ArrayList<comentarios> ListaDeComents;
    LayoutInflater layoutInflater;
    comentarios coments;

    public adaptadorComentarios(Context context, ArrayList<comentarios> listaDeComents) {
        this.context = context;
        ListaDeComents = listaDeComents;
        //this.layoutInflater = layoutInflater;
        //this.coments = coments;
    }

    @Override
    public int getCount() { return ListaDeComents.size(); }

    @Override
    public Object getItem(int position) { return ListaDeComents.get(position); }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.adaptador_comentarios, parent, false);
        TextView UsuarioComentario = itemView.findViewById(R.id.lblUsuarioComentario);
        TextView TextoComentario = itemView.findViewById(R.id.lblTextoComentario);

        try {
            coments = ListaDeComents.get(position);
            UsuarioComentario.setText("@"+coments.getUsaurio()+":");
            TextoComentario.setText(coments.getTexto());
        } catch (Exception e){
            //TextoComentario.setText(e.getMessage());
        }

        return itemView;
    }
}
