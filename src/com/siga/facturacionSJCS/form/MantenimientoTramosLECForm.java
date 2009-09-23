//VERSIONES
//ruben.fernandez : 29/03/2005 Creacion
//

package com.siga.facturacionSJCS.form;

import com.siga.general.MasterForm;

public class MantenimientoTramosLECForm extends MasterForm {

	private String minimo = 	"MINIMOSMI";
	private String maximo =  	"MAXIMOSMI";
	private String retencion =  "RETENCION";
	private String idTramo = 	"IDTRAMOLEC";
	
		
	/**
	 * @return Returns the maximo.
	 */
	public String getMaximo() {
		return (String)this.datos.get(maximo);
	}
	/**
	 * @param maximo The maximo to set.
	 */
	public void setMaximo(String valor) {
		this.datos.put(maximo ,valor);
	}
	/**
	 * @return Returns the minimo.
	 */
	public String getMinimo() {
		return (String)this.datos.get(minimo);
	}
	/**
	 * @param minimo The minimo to set.
	 */
	public void setMinimo(String valor) {
		this.datos.put(minimo ,valor);
	}
	/**
	 * @return Returns the retencion.
	 */
	public String getRetencion() {
		return (String)this.datos.get(retencion);
	}
	/**
	 * @param retencion The retencion to set.
	 */
	public void setRetencion(String valor) {
		this.datos.put(retencion , valor);
	}
	/**
	 * @return Returns the idTramoLEc.
	 */
	public String getIdTramo() {
		return (String)this.datos.get(idTramo);
	}
	/**
	 * @param maximo The idTramoLec to set.
	 */
	public void setIdTramo(String valor) {
		this.datos.put(idTramo,valor);
	}
	
}