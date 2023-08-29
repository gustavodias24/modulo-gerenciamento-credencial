package com.benicio.admkey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benicio.admkey.R;
import com.benicio.admkey.model.EmpresaModel;
import com.benicio.admkey.service.Service;
import com.benicio.admkey.util.MsgModel;
import com.benicio.admkey.util.RetrofitUtil;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder>{
    List<EmpresaModel> lista;
    Context c;

    private Retrofit retrofit = RetrofitUtil.criarRetrofit();
    private Service service = RetrofitUtil.criarService(retrofit);

    public AdapterEmpresa(List<EmpresaModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empresa_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EmpresaModel empresaModel = lista.get(position);

        holder.nomeEmpresa.setText(empresaModel.getNomeEmpresa());
        holder.qtdCredencial.setText(String.format("%d credenciais", empresaModel.getQtdCredenciais()));
        service.pegarLogoEmpresa(empresaModel.get_id()).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if (response.isSuccessful()){
                    String imageBase64 = response.body().getMsg();
                    byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    holder.empresaImage.setImageBitmap(decodedBitmap);
                }
                Log.d("resultado1", "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                Log.d("resultado2", "onResponse: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomeEmpresa, qtdCredencial;
        ImageView empresaImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeEmpresa = itemView.findViewById(R.id.nome_empresa_text);
            qtdCredencial = itemView.findViewById(R.id.qtd_credenciais_text);
            empresaImage = itemView.findViewById(R.id.empresa_image);
        }
    }
}
