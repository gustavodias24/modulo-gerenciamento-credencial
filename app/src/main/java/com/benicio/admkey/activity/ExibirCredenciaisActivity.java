package com.benicio.admkey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.benicio.admkey.R;
import com.benicio.admkey.adapter.AdapterCredencial;
import com.benicio.admkey.adapter.AdapterEmpresa;
import com.benicio.admkey.databinding.ActivityExibirCredenciaisBinding;
import com.benicio.admkey.databinding.ActivityMainBinding;
import com.benicio.admkey.databinding.CriarCredencialLayoutBinding;
import com.benicio.admkey.model.CredencialModel;
import com.benicio.admkey.model.EmpresaModel;
import com.benicio.admkey.service.Service;
import com.benicio.admkey.util.MsgModel;
import com.benicio.admkey.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExibirCredenciaisActivity extends AppCompatActivity {
    private List<CredencialModel> lista = new ArrayList<>();
    private RecyclerView r;
    private Retrofit retrofit;
    private AdapterCredencial adapter;
    private Service service;
    private Dialog dialogCarregando, dialogCriarCredenciais;
    private ActivityExibirCredenciaisBinding binding;
    private EmpresaModel empresaId;

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
        criarDialogAdicionarCredenciamento();

        binding.adicionarCredencialBtn.setOnClickListener( viewCriar -> {
            dialogCriarCredenciais.show();
        });

        getSupportActionBar().setTitle("Gerenciar credenciais");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empresaId = new EmpresaModel(getIntent().getStringExtra("id_empresa"));
        listarCredenciais();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void configurarRecycler(){

        r = binding.credenciaisRecycler;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterCredencial(lista, getApplicationContext());
        r.setAdapter(adapter);

    }

    public void criarDialogAdicionarCredenciamento(){
        AlertDialog.Builder b = new AlertDialog.Builder(ExibirCredenciaisActivity.this);
        CriarCredencialLayoutBinding criarCredencialBinding = CriarCredencialLayoutBinding.inflate(getLayoutInflater());
        criarCredencialBinding.quantidadeCredencialField.getEditText().setText("1");
        criarCredencialBinding.criarCredencialBtn.setOnClickListener( view -> {
            String quantidade = criarCredencialBinding.quantidadeCredencialField.getEditText().getText().toString();
            criarCredencial(quantidade, empresaId);
        });
        b.setView(criarCredencialBinding.getRoot());
        dialogCriarCredenciais = b.create();
    }

    public void criarCredencial(String qtd, EmpresaModel empresaModel){
        dialogCarregando.show();
        service.criarCredencial(qtd, empresaModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                dialogCarregando.dismiss();
                if ( response.isSuccessful() ){
                    Toast.makeText(ExibirCredenciaisActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    listarCredenciais();
                    dialogCriarCredenciais.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }

    public void listarCredenciais(){
        lista.clear();
        dialogCarregando.show();
        service.listarCredencial(empresaId.get_id()).enqueue(new Callback<List<CredencialModel>>() {
            @Override
            public void onResponse(Call<List<CredencialModel>> call, Response<List<CredencialModel>> response) {
                dialogCarregando.dismiss();
                if ( response.isSuccessful() ){
                    lista.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CredencialModel>> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }
}