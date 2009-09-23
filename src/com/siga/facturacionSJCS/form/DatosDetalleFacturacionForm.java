//VERSIONES
//ruben.fernandez : 29/03/2005 Creacion
//

package com.siga.facturacionSJCS.form;

import com.siga.general.MasterForm;

public class DatosDetalleFacturacionForm extends MasterForm {

	private String idPersona = 		"IDPERSONA";
	private String idFacturacion =  "IDFACTURACION";
	private String idInstitucion =  "IDINSTITUCION";
	private String importeTotal = 	"IMPORTETOTAL";
	
	/**
	 * @return Returns the idFacturacion.
	 */
	public String getIdFacturacion() {
		return (String)this.datos.get(idFacturacion);
	}
	/**
	 * @param valor The idFacturacion to set.
	 */
	public void setIdFacturacion(String valor) {
		this.datos.put(idFacturacion , valor);
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getIdInstitucion() {
		return (String)this.datos.get(idInstitucion);
	}
	/**
	 * @param valor The idInstitucion to set.
	 */
	public void setIdInstitucion(String valor) {
		this.datos.put(idInstitucion , valor);
	}
	/**
	 * @return Returns the idPersona.
	 */
	public String getIdPersona() {
		return (String)this.datos.get(idPersona);
	}
	/**
	 * @param valor The idPersona to set.
	 */
	public void setIdPersona(String valor) {
		this.datos.put(idPersona , valor);
	}
	/**
	 * @return Returns the importeTotal.
	 */
	public String getImporteTotal() {
		return (String)this.datos.get(importeTotal);
	}
	/**
	 * @param valor The importeTotal to set.
	 */
	public void setImporteTotal(String valor) {
		this.datos.put(importeTotal , valor);
	}
	
}