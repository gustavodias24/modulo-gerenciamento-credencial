package com.benicio.admkey.service;

import com.benicio.admkey.model.EmpresaModel;
import com.benicio.admkey.util.MsgModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    @POST("criar_empresa")
    Call<MsgModel> criarEmpresa(@Body EmpresaModel empresaModel);

    @POST("listar_empresa")
    Call<List<EmpresaModel>> listarEmpresa();

    @POST("{id}/pegar_logo")
    Call<MsgModel> pegarLogoEmpresa (@Path("id") String id);
}
