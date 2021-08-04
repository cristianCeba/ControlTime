package com.example.controltime;

public class User {

    public String correoElectronico;
    public String contraseña;
    public String Nombre;
    public String Ape;

    public String Movil;
  //  public String Usuario;

    public User (String coreo, String contraseña){
        this.correoElectronico = coreo;
        this.contraseña = contraseña;
    }


    public User ( String contraseña,String Nombre,String Ape,String correo,String Movil){
      //  this.Usuario=Usuario;
        this.contraseña = contraseña;
        this.Nombre=Nombre;
        this.Ape=Ape;
        this.correoElectronico = correo;
        this.Movil=Movil;

    }

}
