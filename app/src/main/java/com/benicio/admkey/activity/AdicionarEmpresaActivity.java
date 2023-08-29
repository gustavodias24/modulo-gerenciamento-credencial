package com.benicio.admkey.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.widget.Toast;

import com.benicio.admkey.R;
import com.benicio.admkey.databinding.ActivityAdicionarEmpresaBinding;
import com.benicio.admkey.model.EmpresaModel;
import com.benicio.admkey.service.Service;
import com.benicio.admkey.util.ImageUtils;
import com.benicio.admkey.util.MsgModel;
import com.benicio.admkey.util.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdicionarEmpresaActivity extends AppCompatActivity {
    private static final int REQUEST_LOGO_EMPRESA_SELECT = 1;
    private ActivityAdicionarEmpresaBinding binding;
    private String imageLogoEmpresa = "";
    private Retrofit retrofit;
    private Service service;
    private Dialog dialogCarregando;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdicionarEmpresaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Adicionar empresa.");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        binding.logoEmpresa.setOnClickListener( logoEmpresaView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_LOGO_EMPRESA_SELECT);
        });

        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarService(retrofit);
        dialogCarregando = RetrofitUtil.criarDialogCarregando(AdicionarEmpresaActivity.this);

        binding.addEmpresaBtn.setOnClickListener( addEmpresaView -> {
            String nomeEmpresa = binding.nomeEmpresaField.getEditText().getText().toString();
            criarEmpresa(new EmpresaModel("", nomeEmpresa, imageLogoEmpresa, 0));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGO_EMPRESA_SELECT && resultCode == RESULT_OK && data != null) {
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();
            binding.logoEmpresa.setImageURI(selectedImageUri);
            imageLogoEmpresa = ImageUtils.imageToBase64Comprimida(selectedImageUri, getApplicationContext());

        }
    }

    private void criarEmpresa(EmpresaModel empresaModel){
        dialogCarregando.show();
        service.criarEmpresa( empresaModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                dialogCarregando.dismiss();
                if (response.isSuccessful()){
                    Toast.makeText(AdicionarEmpresaActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }


}