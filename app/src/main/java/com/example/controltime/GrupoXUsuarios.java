package com.example.controltime;

public class GrupoXUsuarios {
    String id;
    String Usuario;
    String Grupo;
    String TiposUsuario;

    public GrupoXUsuarios(String id, String Usuario, String grupo, String tiposUsuario) {
        this.id = id;
        this.Usuario =Usuario;
        this.Grupo = grupo;
        this.TiposUsuario = tiposUsuario;
    }

    public GrupoXUsuarios() {
        this.id = "";
        Usuario ="";
        Grupo = "";
        TiposUsuario = "";
    }

    public String getId() {
        return id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getGrupo() {
        return Grupo;
    }

    public String getTiposUsuario() {
        return TiposUsuario;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public void setGrupo(String grupo) {
        this.Grupo =  grupo;
    }

    public void setTiposUsuario(String tiposUsuario) {
        this.TiposUsuario = tiposUsuario;
    }
}
