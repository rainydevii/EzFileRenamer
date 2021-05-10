package com.Rainydev.Renombrador;

public class Archivo {
    private String NombreViejo = null;
    private String NombreNuevo = null;

    public Archivo(){

    }

    public Archivo(String NombreViejo, String NombreNuevo){
        this.NombreViejo = NombreViejo;
        this.NombreNuevo = NombreNuevo;
    }

    public void setNombreViejo(String NombreViejo){
        this.NombreViejo = NombreViejo;
    }

    public void setNombreNuevo(String NombreNuevo){
        this.NombreNuevo = NombreNuevo;
    }

    public String getNombreViejo(){
        return NombreViejo;
    }

    public String getNombreNuevo(){
        return NombreNuevo;
    }
}
