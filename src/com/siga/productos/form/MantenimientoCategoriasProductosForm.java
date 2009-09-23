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
public class MantenimientoCategoriasProductosForm extends MasterForm {

	// METODOS SET
	public void setIdTipoProducto(Integer dato) {
		UtilidadesHash.set(this.datos, "_IDTIPOPRODUCTO_", dato);
	}
	public void setDescripcionProducto(String dato) {
		UtilidadesHash.set(this.datos, "_DESCRIPCIONPRODUCTO_", dato);
	}

	// METODOS GET
	public Integer getIdTipoProducto() {
		return UtilidadesHash.getInteger(this.datos, "_IDTIPOPRODUCTO_");
	}
	public String getDescripcionProducto() {
		return UtilidadesHash.getString(this.datos, "_DESCRIPCIONPRODUCTO_");
	}
	
	// BUSCAR
	public void setBuscarIdTipoProducto(Integer dato) {
		UtilidadesHash.set(this.datos, "_BUSCAR_IDTIPOPRODUCTO_", dato);
	}
	public Integer getBuscarIdTipoProducto() {
		return UtilidadesHash.getInteger(this.datos, "_BUSCAR_IDTIPOPRODUCTO_");
	}
}
