package com.benicio.admkey.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;

import com.benicio.admkey.R;
import com.benicio.admkey.adapter.AdapterCredencial;
import com.benicio.admkey.adapter.AdapterEmpresa;
import com.benicio.admkey.databinding.ActivityExibirCredenciaisBinding;
import com.benicio.admkey.databinding.ActivityMainBinding;
import com.benicio.admkey.model.CredencialModel;
import com.benicio.admkey.service.Service;
import com.benicio.admkey.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class ExibirCredenciaisActivity extends AppCompatActivity {
    private List<CredencialModel> lista = new ArrayList<>();
    private RecyclerView r;
    private Retrofit retrofit;
    private AdapterCredencial adapter;
    private Service service;
    private Dialog dialogCarregando;
    private ActivityExibirCredenciaisBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExibirCredenciaisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarService(retrofit);
        dialogCarregando = RetrofitUtil.criarDialogCarregando(ExibirCredenciaisActivity.this);
        configurarRecycler();

    }

    public void configurarRecycler(){

        r = binding.credenciaisRecycler;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterCredencial(lista, getApplicationContext());
        r.setAdapter(adapter);

    }
}