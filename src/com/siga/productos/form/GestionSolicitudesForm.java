/*
 * Created on 03-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.productos.form;

import com.atos.utils.GstDate;
import com.siga.general.MasterForm;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author daniel.campos
 *
 */
public class GestionSolicitudesForm extends MasterForm 
{
	/**
	 * @param buscarFechaDesde The buscarFechaDesde to set.
	 */
	public void setBuscarFechaDesde(String buscarFechaDesde) {
		try {
			if ((buscarFechaDesde!=null) && (!buscarFechaDesde.equals(""))) { 
				buscarFechaDesde = GstDate.getApplicationFormatDate("",buscarFechaDesde);
		 		UtilidadesHash.set(this.datos, "_FECHADESDE_", buscarFechaDesde);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param buscarFechaHasta The buscarFechaHasta to set.
	 */
	public void setBuscarFechaHasta(String buscarFechaHasta) {
		try {
			if ((buscarFechaHasta!=null) && (!buscarFechaHasta.equals(""))) { 
				buscarFechaHasta = GstDate.getApplicationFormatDate("",buscarFechaHasta);
		 		UtilidadesHash.set(this.datos, "_FECHAHASTA_", buscarFechaHasta);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param buscarNombre The buscarNombre to set.
	 */
	public void setBuscarNombre(String buscarNombre) {
		UtilidadesHash.set(this.datos, "_NOMBRE_", buscarNombre);
	}
	/**
	 * @param buscarTipoSolicitud The buscarTipoSolicitud to set.
	 */
	public void setBuscarTipoPeticion(String buscarTipoSolicitud) {
		if ((buscarTipoSolicitud != null) && (!buscarTipoSolicitud.equals("")))
			UtilidadesHash.set(this.datos, "_TIPOSOLICITUD_", buscarTipoSolicitud);
	}
	
	/**
	 * @param buscarEstadoSolicitud The buscarEstadoSolicitud to set.
	 */
	public void setBuscarEstadoPeticion(String buscarEstadoSolicitud) {
		if ((buscarEstadoSolicitud != null) && (!buscarEstadoSolicitud.equals("")))
			UtilidadesHash.set(this.datos, "_ESTADOSOLICITUD_", buscarEstadoSolicitud);
	}
	/**
	 * @param buscarIdPeticion The buscarIdPeticion to set.
	 */
	public void setBuscarIdPeticionCompra(String buscarIdPeticion) {
		if ((buscarIdPeticion!=null )&& (!buscarIdPeticion.equals("")))
			UtilidadesHash.set(this.datos, "_IDPETICION_", buscarIdPeticion);
	}
	/**
	 * @param buscarNifcif The buscarNifcif to set.
	 */
	public void setBuscarNifcif(String buscarNifcif) {
		UtilidadesHash.set(this.datos, "_NIFCIF_", buscarNifcif);
	}
	/**
	 * @param buscarNColegiado The buscarNColegiado to set.
	 */
	public void setBuscarNColegiado(String buscarNColegiado) {
		UtilidadesHash.set(this.datos, "_NCOLEGIADO_", buscarNColegiado);
	}
	/**
	 * @param buscarApellido1 The buscarApellido1 to set.
	 */
	public void setBuscarApellido1(String buscarApellido1) {
		UtilidadesHash.set(this.datos, "_APELLIDO1_", buscarApellido1);
	}
	/**
	 * @param buscarApellido2 The buscarApellido2 to set.
	 */
	public void setBuscarApellido2(String buscarApellido2) {
		UtilidadesHash.set(this.datos, "_APELLIDO2_", buscarApellido2);
	}

	/**
	 * @param idInstitucion
	 */
	public void setIdPeticion (Integer idInstitucion) {
		UtilidadesHash.set(this.datos, "_IDPETICION_EDITAR_", idInstitucion);
	}
	/**
	 * @param idInstitucion
	 */
	public void setIdInstitucion (Integer idInstitucion) {
		UtilidadesHash.set(this.datos, "_IDINSTITUCION_", idInstitucion);
	}
	/**
	 * @param idPersona
	 */
	public void setIdPersona (Long IdPersona) {
		UtilidadesHash.set(this.datos, "_IDPERSONA_", IdPersona);
	}

	/**
	 * @return Returns the buscarApellido2.
	 */
	public String getBuscarApellido2() {
		return UtilidadesHash.getString(this.datos,"_APELLIDO2_");
	}
	/**
	 * @return Returns the buscarApellido1.
	 */
	public String getBuscarApellido1() {
		return UtilidadesHash.getString(this.datos,"_APELLIDO1_");
	}
	/**
	 * @return Returns the buscarFechaDesde.
	 */
	public String getBuscarFechaDesde() {
		return UtilidadesHash.getString(this.datos, "_FECHADESDE_");
	}
	/**
	 * @return Returns the buscarFechaHasta.
	 */
	public String getBuscarFechaHasta() {
		return UtilidadesHash.getString(this.datos, "_FECHAHASTA_");
	}
	/**
	 * @return Returns the buscarIdPeticion.
	 */
	public String getBuscarIdPeticionCompra() {
		return UtilidadesHash.getString(this.datos, "_IDPETICION_");
	}
	/**
	 * @return Returns the buscarNColegiado.
	 */
	public String getBuscarNColegiado() {
		return UtilidadesHash.getString(this.datos, "_NCOLEGIADO_");
	}
	/**
	 * @return Returns the buscarNifcif.
	 */
	public String getBuscarNifcif() {
		return UtilidadesHash.getString(this.datos, "_NIFCIF_");
	}
	/**
	 * @return Returns the buscarNombre.
	 */
	public String getBuscarNombre() {
		return UtilidadesHash.getString(this.datos, "_NOMBRE_");
	}
	/**
	 * @return Returns the buscarTipoSolicitud.
	 */
	public String getBuscarTipoPeticion() {
		return UtilidadesHash.getString(this.datos, "_TIPOSOLICITUD_");
	}
	/**
	 * @return Returns the IdEstado.
	 */
	public String getBuscarEstadoPeticion() {
		return UtilidadesHash.getString(this.datos, "_ESTADOSOLICITUD_");
	}

	/**
	 * @return Returns the IdPeticion.
	 */
	public Long getIdPeticion () {
		return UtilidadesHash.getLong(this.datos, "_IDPETICION_EDITAR_");
	}

	/**
	 * @return Returns the IdInstitucion.
	 */
	public Integer getIdInstitucion () {
		return UtilidadesHash.getInteger(this.datos, "_IDINSTITUCION_");
	}
	
	/**
	 * @return Returns the IdPersona.
	 */
	public Long getIdPersona () {
		return UtilidadesHash.getLong(this.datos, "_IDPERSONA_");
	}
	
	
	/**
	 * Set the ImporteAnticipado
	 */
	public void setImporteAnticipado (Double dato) {
		UtilidadesHash.set(this.datos, "_IMPORTE_ANTICIPADO_", dato);
	}

	/**
	 * @return Returns the ImporteAnticipado
	 */
	public Double getImporteAnticipado () {
		return UtilidadesHash.getDouble(this.datos, "_IMPORTE_ANTICIPADO_");
	}
	
	/**
	 * Set the ImporteAnticipado
	 */
	public void setIdCuenta (Integer dato) {
		UtilidadesHash.set(this.datos, "_IDCUENTA_", dato);
	}

	/**
	 * @return Returns the ImporteAnticipado
	 */
	public Integer getIdCuenta () {
		return UtilidadesHash.getInteger(this.datos, "_IDCUENTA_");
	}

}
