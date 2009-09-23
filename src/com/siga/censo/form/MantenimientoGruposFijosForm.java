/*
 * Created on 01-sep-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;


import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenGruposClienteBean;
import com.siga.general.MasterForm;

/**
 * @author PDM
 * @since 01/09/2006
 *
 */
public class MantenimientoGruposFijosForm extends MasterForm {
	
	
	// METODOS SET
	public void setIdInstitucion(String dato) {
		UtilidadesHash.set(this.datos, CenGruposClienteBean.C_IDINSTITUCION , dato);
	}
	public void setNombre(String dato) {
		UtilidadesHash.set(this.datos, CenGruposClienteBean.C_NOMBRE , dato);
	}
	public void setIdGrupo(String dato) {
		UtilidadesHash.set(this.datos, CenGruposClienteBean.C_IDGRUPO , dato);
	}
	
	
	// METODOS GET
	public String getIdInstitucion() {
		return UtilidadesHash.getString(this.datos, CenGruposClienteBean.C_IDINSTITUCION);
	}
	public String getNombre() {
		return UtilidadesHash.getString(this.datos, CenGruposClienteBean.C_NOMBRE);
	}
	public String getIdGrupo() {
		return UtilidadesHash.getString(this.datos, CenGruposClienteBean.C_IDGRUPO);
	}
		
}
