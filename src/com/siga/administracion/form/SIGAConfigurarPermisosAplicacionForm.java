package com.siga.administracion.form;

import com.siga.general.MasterForm;

public class SIGAConfigurarPermisosAplicacionForm extends MasterForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8709311403495260667L;
	private String idPerfil="";
	    
    public String getIdPerfil()
    {
        return idPerfil;
    }
    
    public void setIdPerfil(String idPerfil)
    {
        this.idPerfil = idPerfil;
    }
}