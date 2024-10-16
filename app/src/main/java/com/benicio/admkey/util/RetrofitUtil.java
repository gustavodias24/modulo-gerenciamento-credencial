package com.benicio.admkey.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.benicio.admkey.databinding.CarregarLayoutBinding;
import com.benicio.admkey.service.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    public static Dialog criarDialogCarregando(Activity c){
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setView(CarregarLayoutBinding.inflate(c.getLayoutInflater()).getRoot());
        return b.create();
    }
    public static Retrofit criarRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("http://191.252.110.178:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Service criarService(Retrofit retrofit){
        return retrofit.create(Service.class);
    }
}
