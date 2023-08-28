package com.benicio.admkey.model;

public class EmpresaModel {
    String _id, nomeEmpresa;
    int qtdCredenciais;

    public String get_id() {
        return _id;
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
