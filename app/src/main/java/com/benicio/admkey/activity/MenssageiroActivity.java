package com.benicio.admkey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.benicio.admkey.R;
import com.benicio.admkey.databinding.ActivityMenssageiroBinding;

public class MenssageiroActivity extends AppCompatActivity {
    private WebView webView;
    private ActivityMenssageiroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenssageiroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        webView = binding.webview;

        // Configurar configurações do WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // Permitir JavaScript
        webSettings.setDomStorageEnabled(true);  // Permitir armazenamento DOM
        webSettings.setLoadsImagesAutomatically(true);  // Carregar imagens automaticamente

        // Carregar uma URL no WebView
        String url = "https://comunicao-clientes-kaizen.vercel.app/";  // Substitua pela URL desejada
        webView.loadUrl(url);

        getSupportActionBar().setTitle("Gestor de mensagem");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}