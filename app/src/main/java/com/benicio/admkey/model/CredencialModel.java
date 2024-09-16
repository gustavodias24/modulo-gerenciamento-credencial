package com.benicio.admkey.model;

public class CredencialModel {
    String _id, data_ativacao, dispositivo;
    Boolean ativa, bloqueada = false;
    UsuarioModel usuario;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getData_ativacao() {
        return data_ativacao;
    }

    public void setData_ativacao(String data_ativacao) {
        this.data_ativacao = data_ativacao;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public Boolean getBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(Boolean bloqueada) {
        this.bloqueada = bloqueada;
    }
}
