package com.siga.administracion.form;

import com.siga.general.MasterForm;

public class SIGAAsignarUsuariosGrupoForm extends MasterForm
{
	private String nombreBusqueda="";
	private String idGrupoBusqueda="";
	private String idRolBusqueda="";
	    
    public String getNombreBusqueda()
    {
        return nombreBusqueda;
    }
    
    public void setNombreBusqueda(String nombreBusqueda)
    {
        this.nombreBusqueda = nombreBusqueda;
    }

    public String getIdGrupoBusqueda() 
    {
        return idGrupoBusqueda;
    }
    
    public void setIdGrupoBusqueda(String idGrupoBusqueda)
    {
        this.idGrupoBusqueda = idGrupoBusqueda;
    }

    public String getIdRolBusqueda() 
    {
        return idRolBusqueda;
    }
    
    public void setIdRolBusqueda(String idRolBusqueda)
    {
        this.idRolBusqueda = idRolBusqueda;
    }
}