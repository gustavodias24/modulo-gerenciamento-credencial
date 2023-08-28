package com.benicio.admkey.util;

import com.benicio.admkey.service.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    public static Retrofit criarRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Service criarService(Retrofit retrofit){
        return retrofit.create(Service.class);
    }
}
