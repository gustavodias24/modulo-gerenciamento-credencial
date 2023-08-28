package com.benicio.admkey.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.benicio.admkey.adapter.AdapterEmpresa;
import com.benicio.admkey.databinding.ActivityMainBinding;
import com.benicio.admkey.model.EmpresaModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RecyclerView r;
    private List<EmpresaModel> lista = new ArrayList<>();
    private AdapterEmpresa adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        configurarRecycler();
    }

    public void configurarRecycler(){

        r = binding.empresasRecycler;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterEmpresa(lista, getApplicationContext());
        r.setAdapter(adapter);

    }
}