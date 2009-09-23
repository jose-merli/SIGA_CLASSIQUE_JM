//VERSIONES
//ruben.fernandez : 07/04/2005 Creacion
//

package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.general.MasterForm;

public class MantenimientoProcedimientosForm extends MasterForm {

	private String idProcedimiento = 	"IDPROCEDIMIENTO";
	private String nombre =	 			"NOMBRE";
	private String precio =	  			"PRECIO";
	private String puntos = 			"PUNTOS";
	private String idJurisdiccion =		ScsProcedimientosBean.C_IDJURISDICCION;
	private String idAcreditacion =		"IDACREDITACION";
	private String porcentaje =			"PORCENTAJE";
	private String codigo =				"CODIGO";
	private String codigoBusqueda =				"CODIGOBUSQUEDA";
	private String complemento = 		ScsProcedimientosBean.C_COMPLEMENTO;
	private String vigente = 		    ScsProcedimientosBean.C_VIGENTE;


	/**
	 * @return Returns the refresco.
	 */
	public String getRefresco() {
		return UtilidadesHash.getString(this.datos, "REFRESCO");
	}
	/**
	 * @param idJurisdiccion The refresco to set.
	 */
	public void setRefresco(String d) {
		UtilidadesHash.set (this.datos, "REFRESCO", d);
	}

	/**
	 * @return Returns the idJurisdiccion.
	 */
	public Integer getPorcentaje() {
		return UtilidadesHash.getInteger(this.datos, porcentaje);
	}
	/**
	 * @param idJurisdiccion The idJurisdiccion to set.
	 */
	public void setPorcentaje(Integer _porcentaje) {
		UtilidadesHash.set (this.datos, this.porcentaje,_porcentaje);
	}
	/**
	 * @return Returns the idJurisdiccion.
	 */
	public Integer getIdAcreditacion() {
		return UtilidadesHash.getInteger(this.datos, idAcreditacion);
	}
	/**
	 * @param idJurisdiccion The idJurisdiccion to set.
	 */
	public void setIdAcreditacion(Integer _idAcreditacion) {
		UtilidadesHash.set (this.datos, this.idAcreditacion ,_idAcreditacion);
	}

	/**
	 * @return Returns the idJurisdiccion.
	 */
	public Integer getJurisdiccion() {
		return UtilidadesHash.getInteger(this.datos, idJurisdiccion);
	}
	/**
	 * @param idJurisdiccion The idJurisdiccion to set.
	 */
	public void setJurisdiccion(Integer _idJurisdiccion) {
		UtilidadesHash.set (this.datos, this.idJurisdiccion ,_idJurisdiccion);
	}
	/**
	 * @return Returns the idProcedimiento.
	 */
	public String getIdProcedimiento() {
		return (String)this.datos.get(idProcedimiento);
	}
	/**
	 * @param idProcedimientos The idProcedimientos to set.
	 */
	public void setIdProcedimiento(String valor) {
		this.datos.put(idProcedimiento , valor);
	}
	/**
	 * @return Returns the precio.
	 */
	public String getPrecio() {
		return (String)this.datos.get(precio);
	}
	/**
	 * @param importe The precio to set.
	 */
	public void setImporte(String valor) {
		this.datos.put(precio , valor);
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return (String)this.datos.get(nombre);
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String valor) {
		this.datos.put(nombre , valor);
	}
	/**
	 * @return Returns the puntos.
	 */
	public String getPuntos() {
		return (String)this.datos.get(puntos);
	}
	/**
	 * @param puntos The puntos to set.
	 */
	public void setPuntos(String valor) {
		this.datos.put(puntos ,valor);
	}
	public String getCodigo() {
		return (String)this.datos.get(codigo);
	}
	public void setCodigo(String valor) {
		this.datos.put(codigo , valor);
	}
	
	public String getCodigoBusqueda() {
		return (String)this.datos.get(codigoBusqueda);
	}
	public void setCodigoBusqueda(String valor) {
		this.datos.put(codigoBusqueda , valor);
	}
	
	public String getComplemento() {
		return UtilidadesHash.getString(this.datos, complemento);
	}
	public void setComplemento(String valor) {
		UtilidadesHash.set(this.datos, complemento, valor);
	}	
	public String getVigente() {
		return UtilidadesHash.getString(this.datos, vigente);
	}
	public void setVigente(String valor) {
		UtilidadesHash.set(this.datos, vigente, valor);
	}	
}