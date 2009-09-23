/*
 * Created on 14-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.productos.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author daniel.campos
 *
 */
public class MantenimientoCategoriasServiciosForm extends MasterForm {

	// METODOS SET
	public void setIdTipoServicio(Integer dato) {
		UtilidadesHash.set(this.datos, "_IDTIPOSERVICIO_", dato);
	}
	public void setDescripcionServicio(String dato) {
		UtilidadesHash.set(this.datos, "_DESCRIPCIONSERVICIO_", dato);
	}

	// METODOS GET
	public Integer getIdTipoServicio() {
		return UtilidadesHash.getInteger(this.datos, "_IDTIPOSERVICIO_");
	}
	public String getDescripcionServicio() {
		return UtilidadesHash.getString(this.datos, "_DESCRIPCIONSERVICIO_");
	}
	
	// BUSCAR
	public void setBuscarIdTipoServicio(Integer dato) {
		UtilidadesHash.set(this.datos, "_BUSCAR_IDTIPOSERVICIO_", dato);
	}
	public Integer getBuscarIdTipoServicio() {
		return UtilidadesHash.getInteger(this.datos, "_BUSCAR_IDTIPOSERVICIO_");
	}
}
