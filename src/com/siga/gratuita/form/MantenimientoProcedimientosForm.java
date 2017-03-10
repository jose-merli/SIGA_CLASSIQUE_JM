//VERSIONES
//ruben.fernandez : 07/04/2005 Creacion
//

package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.general.MasterForm;

public class MantenimientoProcedimientosForm extends MasterForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5984496713406572334L;
	private String idProcedimiento = 	"IDPROCEDIMIENTO";
	private String nombre =	 			"NOMBRE";
	private String precio =	  			"PRECIO";
	private String idJurisdiccion =		ScsProcedimientosBean.C_IDJURISDICCION;
	private String idAcreditacion =		"IDACREDITACION";
	private String porcentaje =			"PORCENTAJE";
	private String nigNumProcedimiento = "NIGNUMPROCEDIMIENTO";
	private String codigo =				"CODIGO";
	private String codigoBusqueda =				"CODIGOBUSQUEDA";
	private String complemento = 		ScsProcedimientosBean.C_COMPLEMENTO;
	private String vigente = 		    ScsProcedimientosBean.C_VIGENTE;
	private String permitirAniadirLetrado =  ScsProcedimientosBean.C_PERMITIRANIADIRLETRADO;
	private String fechaDesdeVigor;
	private String fechaHastaVigor;
	private String codigoExt="CODIGOEXT";
	//private String codSubtarifa="CODSUBTARIFA";
	
	

	public String getCodExtAcreditacion() {
		
		return UtilidadesHash.getString(this.datos, "CODEXTACREDITACION");
		
	}
	public void setCodExtAcreditacion(String codExtAcreditacion) {
		UtilidadesHash.set (this.datos, "CODEXTACREDITACION", codExtAcreditacion);
	}
	
	public String getCodSubtarifa() {
		
		return UtilidadesHash.getString(this.datos, "CODSUBTARIFA");
		
	}
	public void setCodSubtarifa(String codSubtarifa) {
		UtilidadesHash.set (this.datos, "CODSUBTARIFA", codSubtarifa);
	}
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
	public Integer getNigNumProcedimiento() {
		return UtilidadesHash.getInteger(this.datos, nigNumProcedimiento);
	}
	/**
	 * @param idJurisdiccion The idJurisdiccion to set.
	 */
	public void setNigNumProcedimiento(Integer _nigNumProcedimiento) {
		UtilidadesHash.set (this.datos, this.nigNumProcedimiento,_nigNumProcedimiento);
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

	public String getCodigo() {
		return (String)this.datos.get(codigo);
	}
	public void setCodigo(String valor) {
		this.datos.put(codigo , valor);
	}
	
	public String getCodigoExt() {
		return (String)this.datos.get(codigoExt);
	}
	public void setCodigoExt(String valor) {
		this.datos.put(codigoExt , valor);
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
	
	public String getPermitirAniadirLetrado() {
		return UtilidadesHash.getString(this.datos, permitirAniadirLetrado);
	}
	public void setPermitirAniadirLetrado(String valor) {
		UtilidadesHash.set(this.datos, permitirAniadirLetrado, valor);
	}
	
	public String getFechaDesdeVigor() {
		return fechaDesdeVigor;
	}
	public void setFechaDesdeVigor(String fechaDesdeVigor) {
		this.fechaDesdeVigor = fechaDesdeVigor;
	}
	public String getFechaHastaVigor() {
		return fechaHastaVigor;
	}
	public void setFechaHastaVigor(String fechaHastaVigor) {
		this.fechaHastaVigor = fechaHastaVigor;
	}	
	
	
}