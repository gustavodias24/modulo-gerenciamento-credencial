package com.benicio.admkey.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.benicio.admkey.adapter.AdapterEmpresa;
import com.benicio.admkey.databinding.ActivityMainBinding;
import com.benicio.admkey.model.EmpresaModel;
import com.benicio.admkey.service.Service;
import com.benicio.admkey.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RecyclerView r;
    private Retrofit retrofit;
    private Service service;
    private Dialog dialogCarregando;
    private List<EmpresaModel> lista = new ArrayList<>();
    private AdapterEmpresa adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dialogCarregando = RetrofitUtil.criarDialogCarregando(this);

        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarService(retrofit);
        configurarRecycler();

        binding.empresaAddFab.setOnClickListener( viewAdd -> {
            startActivity(new Intent(getApplicationContext(), AdicionarEmpresaActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        listarEmpresas();

    }

    public void configurarRecycler(){

        r = binding.empresasRecycler;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterEmpresa(lista, getApplicationContext());
        r.setAdapter(adapter);

    }

    public void listarEmpresas(){
        lista.clear();
//        dialogCarregando.show();
        service.listarEmpresa().enqueue(new Callback<List<EmpresaModel>>() {
            @Override
            public void onResponse(Call<List<EmpresaModel>> call, Response<List<EmpresaModel>> response) {
//                dialogCarregando.dismiss();
                if ( response.isSuccessful()){
                    lista.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<EmpresaModel>> call, Throwable t) {
//                dialogCarregando.dismiss();
            }
        });
    }

}