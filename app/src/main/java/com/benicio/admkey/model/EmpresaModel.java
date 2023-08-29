package com.benicio.admkey.model;

public class EmpresaModel {
    String _id, nomeEmpresa, logoEmpresa;
    int qtdCredenciais;

    public EmpresaModel(String _id) {
        this._id = _id;
    }

    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    public String get_id() {
        return _id;
    }

    public EmpresaModel(String _id, String nomeEmpresa, String logoEmpresa, int qtdCredenciais) {
        this._id = _id;
        this.nomeEmpresa = nomeEmpresa;
        this.logoEmpresa = logoEmpresa;
        this.qtdCredenciais = qtdCredenciais;
    }

    public EmpresaModel() {
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public int getQtdCredenciais() {
        return qtdCredenciais;
    }

    public void setQtdCredenciais(int qtdCredenciais) {
        this.qtdCredenciais = qtdCredenciais;
    }
}
