package com.benicio.admkey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.benicio.admkey.R;
import com.benicio.admkey.adapter.AdapterCredencial;
import com.benicio.admkey.adapter.AdapterEmpresa;
import com.benicio.admkey.databinding.ActivityExibirCredenciaisBinding;
import com.benicio.admkey.databinding.ActivityMainBinding;
import com.benicio.admkey.databinding.CriarCredencialLayoutBinding;
import com.benicio.admkey.databinding.LayoutAcaoCredencialBinding;
import com.benicio.admkey.model.CredencialModel;
import com.benicio.admkey.model.EmpresaModel;
import com.benicio.admkey.service.Service;
import com.benicio.admkey.util.MsgModel;
import com.benicio.admkey.util.RecyclerItemClickListener;
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
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExibirCredenciaisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarService(retrofit);
        dialogCarregando = RetrofitUtil.criarDialogCarregando(ExibirCredenciaisActivity.this);
        configurarRecycler();
        criarDialogAdicionarCredenciamento();

        binding.adicionarCredencialBtn.setOnClickListener(viewCriar -> {
            dialogCriarCredenciais.show();
        });

        getSupportActionBar().setTitle("Gerenciar credenciais");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empresaId = new EmpresaModel(getIntent().getStringExtra("id_empresa"));

        binding.baixarCredencialBtn.setOnClickListener(baixarView -> {
            AlertDialog.Builder b = new AlertDialog.Builder(ExibirCredenciaisActivity.this);
            b.setPositiveButton("Copiar apenas as inativadas", (dialogInterface, i) -> copiarParaTransferencia("0"));

            b.setNegativeButton("Copiar tudo", (dialogInterface, i) -> copiarParaTransferencia("1"));
            b.show();
        });

        r.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                r,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        CredencialModel credencialClicada = lista.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ExibirCredenciaisActivity.this);

                        LayoutAcaoCredencialBinding credencialBinding = LayoutAcaoCredencialBinding.inflate(getLayoutInflater());
                        credencialBinding.copiar.setOnClickListener(v -> {
                            Toast.makeText(ExibirCredenciaisActivity.this, "Credencial copiada", Toast.LENGTH_SHORT).show();
                            clipboardManager.setPrimaryClip(ClipData.newPlainText("credencial", credencialClicada.get_id()));
                        });

                        credencialBinding.swAtivada.setChecked(credencialClicada.getAtiva());
                        credencialBinding.swBlock.setChecked(credencialClicada.getBloqueada());

                        credencialBinding.swAtivada.setOnClickListener(v -> {
                            credencialClicada.setAtiva(credencialBinding.swAtivada.isChecked());
                            alterarStatus(credencialClicada);
                        });

                        credencialBinding.swBlock.setOnClickListener( v -> {
                            credencialClicada.setBloqueada(credencialBinding.swBlock.isChecked());
                            alterarStatus(credencialClicada);
                        });

                        builder.setView(credencialBinding.getRoot());

                        builder.create().show();
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

    public void alterarStatus(CredencialModel credencialModel) {
        dialogCarregando.show();
        service.alterarStatus(credencialModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                dialogCarregando.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(ExibirCredenciaisActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    listarCredenciais();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarCredenciais();
    }

    public void copiarParaTransferencia(String todas) {
        dialogCarregando.show();
        service.credencialForCopy(empresaId.get_id(), todas).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                dialogCarregando.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(ExibirCredenciaisActivity.this, "Credenciais copiadas!", Toast.LENGTH_SHORT).show();
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("credenciais", response.body().getMsg()));
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void configurarRecycler() {

        r = binding.credenciaisRecycler;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterCredencial(lista, getApplicationContext());
        r.setAdapter(adapter);

    }

    public void criarDialogAdicionarCredenciamento() {
        AlertDialog.Builder b = new AlertDialog.Builder(ExibirCredenciaisActivity.this);
        CriarCredencialLayoutBinding criarCredencialBinding = CriarCredencialLayoutBinding.inflate(getLayoutInflater());
        criarCredencialBinding.quantidadeCredencialField.getEditText().setText("1");
        criarCredencialBinding.criarCredencialBtn.setOnClickListener(view -> {
            String quantidade = criarCredencialBinding.quantidadeCredencialField.getEditText().getText().toString();
            criarCredencial(quantidade, empresaId);
        });
        b.setView(criarCredencialBinding.getRoot());
        dialogCriarCredenciais = b.create();
    }

    public void criarCredencial(String qtd, EmpresaModel empresaModel) {
        dialogCarregando.show();
        service.criarCredencial(qtd, empresaModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                dialogCarregando.dismiss();
                if (response.isSuccessful()) {
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

    public void listarCredenciais() {
        lista.clear();
        dialogCarregando.show();
        service.listarCredencial(empresaId.get_id()).enqueue(new Callback<List<CredencialModel>>() {
            @Override
            public void onResponse(Call<List<CredencialModel>> call, Response<List<CredencialModel>> response) {
                dialogCarregando.dismiss();
                if (response.isSuccessful()) {
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