/*
 * Created on 28-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import com.atos.utils.UsrBean;

/**
 * @author esdras.martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class VisibilidadBeanAdm extends MasterBeanAdministrador 
{
	protected String visibilidad=null;
	
	VisibilidadBeanAdm(String tabla, UsrBean usuario) {
		super(tabla, usuario);
	}
	
	public void calculaVisibilidad(UsrBean bean, long idPersona, int idInstitucion ) {
		
	}
}
