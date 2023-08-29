package com.benicio.admkey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.benicio.admkey.adapter.AdapterEmpresa;
import com.benicio.admkey.databinding.ActivityMainBinding;
import com.benicio.admkey.model.EmpresaModel;
import com.benicio.admkey.service.Service;
import com.benicio.admkey.util.RecyclerItemClickListener;
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
    private List<EmpresaModel> lista = new ArrayList<>();
    private AdapterEmpresa adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarService(retrofit);
        configurarRecycler();

        binding.empresaAddFab.setOnClickListener( viewAdd -> {
            startActivity(new Intent(getApplicationContext(), AdicionarEmpresaActivity.class));
        });

        r.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                r,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        EmpresaModel empresaClicada = lista.get(position);
                        Intent i = new Intent(getApplicationContext(), ExibirCredenciaisActivity.class);
                        i.putExtra("id_empresa", empresaClicada.get_id());
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
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
        binding.carregamento.setVisibility(View.VISIBLE);
        service.listarEmpresa().enqueue(new Callback<List<EmpresaModel>>() {
            @Override
            public void onResponse(Call<List<EmpresaModel>> call, Response<List<EmpresaModel>> response) {
                binding.carregamento.setVisibility(View.GONE);
                if ( response.isSuccessful()){
                    lista.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<EmpresaModel>> call, Throwable t) {
                binding.carregamento.setVisibility(View.GONE);
            }
        });
    }

}