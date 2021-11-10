package com.example.controltime;

import java.io.Serializable;

public class ClsUsuarioPermiso implements Serializable {

    String nombre;
    String tipoPemriso;
    String inicioPermiso;
    String finPermiso;
    String correo;
    String tipoUsuario;
    String grupoUsuario;
    int codigo;

    public ClsUsuarioPermiso (){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoPemriso() {
        return tipoPemriso;
    }

    public void setTipoPemriso(String tipoPemriso) {
        this.tipoPemriso = tipoPemriso;
    }

    public String getInicioPermiso() {
        return inicioPermiso;
    }

    public void setInicioPermiso(String inicioPermiso) {
        this.inicioPermiso = inicioPermiso;
    }

    public String getFinPermiso() {
        return finPermiso;
    }

    public void setFinPermiso(String finPermiso) {
        this.finPermiso = finPermiso;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getGrupoUsuario() {
        return grupoUsuario;
    }

    public void setGrupoUsuario(String grupoUsuario) {
        this.grupoUsuario = grupoUsuario;
    }

}
