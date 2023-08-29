package com.benicio.admkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benicio.admkey.R;
import com.benicio.admkey.model.CredencialModel;

import java.util.List;

public class AdapterCredencial extends RecyclerView.Adapter<AdapterCredencial.MyViewHolder>{
    List<CredencialModel> lista;
    Context c;

    public AdapterCredencial(List<CredencialModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.credencial_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CredencialModel credencialModel = lista.get(position);
        String infoUser = "";
        if ( credencialModel.getUsuario() != null){
            infoUser = "\nAtivada por:" + credencialModel.getUsuario().getNome() + " no dia " + credencialModel.getData_ativacao() + "\n" +
            "Email: " + credencialModel.getUsuario().getEmail() + "\n" +
            "Telefone: " + credencialModel.getUsuario().getTelefone() + "\n" +
            "Login: " + credencialModel.getUsuario().getLogin();
        }
        holder.status.setText(
                "Credencial Status:\n" + "Ativa: "+ credencialModel.getAtiva() + "\n" + infoUser
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status_text);
        }
    }
}
